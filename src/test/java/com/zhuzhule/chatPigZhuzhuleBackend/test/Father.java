package zhuzhule.chatPigZhuzhuleBackend.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

abstract class Father {
  public String id;

  public abstract void printId();

  public void sayHello() {
    System.err.println("iii");
  }
}

class Son extends Father {

  @Override
  public void printId() {}
}

class Test {

  public static void main(String[] args) {
    //    Son son = new Son();
    //    son.printId();
    //    son.sayHello();
    JSONObject jsonObject = new JSONObject();

    JSONArray jsonSubArray = new JSONArray();
    JSONObject subObject1 = new JSONObject();
    JSONObject subObject2 = new JSONObject();
    subObject1.put("role", "user");
    subObject1.put("content", "What's the highest mountain in the world?");
    subObject2.put("role", "assistant");
    subObject2.put("content", "The highest mountain in the world is Mount Everest.");
    jsonSubArray.add(subObject1);
    jsonSubArray.add(subObject2);
    jsonObject.put("messages", jsonSubArray);

    System.err.println(jsonObject.toJSONString());

    //    chatContentService.
    //    List<ChatContent> chatContents =
  }

  //  public String getJSONString(String userMsg) {
  //    JSONObject jsonObject = new JSONObject();
  //
  //    JSONArray jsonMessagesArray = new JSONArray();
  //  }
}
