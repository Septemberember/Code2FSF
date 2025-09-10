# Code2FSF

A framework for **Program Comprehension and Evolution** via Large Language Models using Automated Testing-Based Formal Verification.

---

## 📦 Prerequisites

### Z3-Solver

* Install **Z3-Solver 4.15.0**

### LLMs

* Create a `.txt` file in the `resources/config` directory to store your API URL and API key.
* The file name should match your model's name, e.g., `gpt-4o.txt`.
* Example content of `gpt-4o.txt`:

  ```ini
  [url]=<your gpt-4o api url>
  [apikey]=<your api key>
  ```

---

## 🚀 Experiment Execution Steps

0. The packaged JAR `Code2FSF-1.0.jar` is located in the `experiment-kit` directory, and the source codes are in the `src` directory.
1. Place the categorized dataset directories under the `dataset` folder.
2. Write the correct API key configuration file in `resources/config`.

   * The default prompt template is `prompt2.1.txt`, located in the `fewShot` directory (you may customize it).
3. Ensure the following are installed:

   * **openjdk-17**  (check with `java -version`; it should display JDK 17)
   * **z3-solver**
4. Run program comprehension experiments:

   ```bash
   cd experiment-kit
   java -jar Code2FSF-1.0.jar -d [target dataset dir] -r [max rounds] -m [llm model] -en [experiment name]
   ```

### Example

```bash
java -jar Code2FSF-1.0.jar -d dataset -r 10 -m gpt-4o -en test-conversational
```

**Explanation:**

* `-d dataset`: use dataset in `dataset` folder
* `-r 10`: maximum 10 conversation rounds
* `-m gpt-4o`: specify the model `gpt-4o`
* `-en test-conversational`: set experiment name to `test-conversational`

---

## 📊 Experiment Output

All experiment results are stored in the directory:

```
resources/experiment/[experiment name]
```

Each experiment contains:

* `succDataset/` → Successfully generated & verified programs
* `failedDataset/` → Programs that failed generation or verification
* `exceptionDataset/` → Programs that failed due to unknown reasons
* `[modelName]/` → Complete conversation history for all programs
* `summary.txt` → Automatically calculated basic statistics

💡 If the experiment is interrupted unexpectedly, simply re-run the same command to resume.

---
