package org.zed.tcg;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import org.zed.log.LogManager;
import org.zed.trans.ExecutionPathPrinter;
import org.zed.trans.TransWorker;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.zed.tcg.TestCaseAutoGenerator.generateTestCaseRandomlyUnderExpr;
import static org.zed.tcg.TestCaseAutoGenerator.getDefaultValueOfType;

public class ExecutionEnabler {
    public static final int Z3_GENERATION = 1;
    public static final int RANDOMLY_GENERATION = 2;

    public static String insertMainMdInSSMP(String ssmp,String mainMd) {
        JavaParser parser = new JavaParser();
        CompilationUnit cu = parser.parse(ssmp).getResult().get();
        String className = cu.getTypes().get(0).getNameAsString();
        Optional<ClassOrInterfaceDeclaration> classOpt = cu.getClassByName(className);
        MethodDeclaration mainMethodDec = parser.parseMethodDeclaration(mainMd).getResult().get();
        classOpt.get().addMember(mainMethodDec);
        return cu.toString();
    }

    public static String constructConstrain(String T,List<String> preConstrains){
        if(preConstrains == null || preConstrains.isEmpty()){
            return T;
        }
        StringBuilder consExpr = new StringBuilder();
        if(T.startsWith("(")){
            consExpr.append(T);
        }else {
            consExpr.append('(').append(T).append(")");
        }
        for(String con : preConstrains){
            consExpr.append(" && ");
            consExpr.append(" !");
            if(con.startsWith("(")){
                consExpr.append(con);
            } else {
                consExpr.append('(').append(con).append(")");
            }
        }
        return consExpr.toString();
    }

    public static String generateMainMdUnderExpr(String T, List<String> preconditions, String ssmp) {
        String conExpr = constructConstrain(T, preconditions);
        System.out.println("：" + conExpr);
        if(ExecutionPathPrinter.ssmpHasLoopStmt(ssmp)){
            System.out.println("Testcase generating randomly!");
            return generateMainMdRandomly(conExpr, ssmp);
        }else{
            System.out.println("Testcase generating by Z3!");
            return generateMainMdByZ3(conExpr, ssmp);
        }
    }

    public static String buildMainString(String ssmp,HashMap<String,String> testCaseMap){
        //1. program
        JavaParser parser = new JavaParser();
        CompilationUnit cu = parser.parse(ssmp).getResult().get();
        String className = cu.getTypes().get(0).getNameAsString();
        MethodDeclaration md = getFirstStaticMethod(ssmp);

        List<Parameter> parameters = md.getParameters();
        //2. main
        StringBuilder builder = new StringBuilder();
        builder.append("public static void main(String[] args) {\n");
        if(testCaseMap == null){
            System.out.println("main,testCaseMap");
            return null;
        }

        //4.  testCasemain static 
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                System.out.println(parameter.getName().toString());
                String value = testCaseMap.get(parameter.getName().asString());

                //
                if(value == null){
                    value = getDefaultValueOfType(parameter.getTypeAsString());
                }
                if("char".equals(parameter.getTypeAsString())){
                    value = "'"+value+"'";
                }
                builder.append(parameter.getTypeAsString())
                        .append(" ")
                        .append(parameter.getNameAsString())
                        .append(" = ")
                        .append(value)
                        .append(";\n");
            }
        }
        //5.
        if (!md.getType().isVoidType()) {
            builder.append("    ").append(md.getType()).append(" result = ");
        }

        builder.append(className).append(".").append(md.getNameAsString()).append("(");
        for (int i = 0; i < parameters.size(); i++) {
            if (i > 0) builder.append(", ");//,
            builder.append(parameters.get(i).getNameAsString());
        }
        builder.append(");\n");
        builder.append("}");
        return builder.toString();
    }



    public static String generateMainMdRandomly(String expr, String ssmp) {
        MethodDeclaration md = getFirstStaticMethod(ssmp);
        HashMap<String, String> testCaseMap = generateTestCaseRandomlyUnderExpr(expr, md);
        if(testCaseMap == null){
            System.out.println("main,testCaseMap");
            return null;
        }
        return buildMainString(ssmp,testCaseMap);
    }

    public static String generateMainMdByZ3(String expr,String ssmp){
        //1. program
        HashMap<String, String> testCaseMap = TestCaseAutoGenerator.generateTestCaseByZ3(expr,ssmp);
        if(testCaseMap.get("ERROR") != null){
            System.out.println(testCaseMap.get("ERROR") + "main,testCaseMap");
            return "ERROR:" + testCaseMap.get("ERROR");
        }
        return buildMainString(ssmp,testCaseMap);
    }

    public static MethodDeclaration getFirstStaticMethod(String program){
        JavaParser parser = new JavaParser();
        // Java
        CompilationUnit cu = parser.parse(program).getResult().get();

        // 
        String className = cu.getTypes().get(0).getNameAsString();
        Optional<ClassOrInterfaceDeclaration> classOpt = cu.getClassByName(className);
        if (classOpt.isEmpty()) {
            return null;
        }

        // （main）
        Optional<MethodDeclaration> staticMethodOpt = cu.findAll(MethodDeclaration.class).stream()
                .filter(m -> m.isStatic() && !m.getNameAsString().equals("main"))
                .findFirst();
        return staticMethodOpt.get();
    }

    public static List<Parameter> getParamsOfOneStaticMethod(String program){
        CompilationUnit cu = new JavaParser().parse(program).getResult().get();
        String className = cu.getTypes().get(0).getNameAsString();
        Optional<MethodDeclaration> staticMethodOpt = cu.findAll(MethodDeclaration.class).stream()
                .filter(m -> m.isStatic() && !m.getNameAsString().equals("main"))
                .findFirst();
        if (staticMethodOpt.isEmpty()) {
            System.out.println("，: " + className);
            return null;
        }
        MethodDeclaration staticMethod = staticMethodOpt.get();
        List<Parameter> parameters = staticMethod.getParameters();
        return parameters;
    }

//    public static void main(String[] args) {
//        String filePath = "resources/dataset/someBench/LeapYear_Original.java";
//        String logPath = "resources/log/deepseek-chat/AllCode2PartLM/log-LeapYear_Original.txt";
//        List<String[]> lastestFSFFromLog = LogManager.getLastestFSFFromLog(logPath);
//        for(String[] td : lastestFSFFromLog){
//            String t = td[0];
//            String program = LogManager.file2String(filePath);
//            String ssmp = TransWorker.trans2SSMP(program);
//            String s = generateMainMdByZ3(t, ssmp);
//            System.out.println(s);
//        }
//
//    }

}
