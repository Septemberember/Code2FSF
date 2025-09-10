package org.zed;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.zed.evolution.CodeGenerator;
import org.zed.llm.ModelConfig;
import org.zed.log.LogManager;

import java.io.IOException;

import static org.zed.verification.FSFGenerator.*;
import static org.zed.trans.TransWorker.pickSSMPCodes;


/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */

    public void testApp(){
            System.out.println("hello world");
        }

    public void testApp4() throws Exception {
        System.out.println("hello world!");
    }

}
