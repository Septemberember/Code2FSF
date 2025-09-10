package org.zed.llm;

import java.io.IOException;

public class ModelCaller {
    public static ModelMessage make1RoundConversation(ModelPrompt prompt,ModelConfig mc){
        ModelClient client = new ModelClient(mc);
        try {
            //DeepSeekApi
            ModelResponse response = client.call(prompt);
            //jsonchatResponse
            //choice，choice
            return response.getChoices().get(0).getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
