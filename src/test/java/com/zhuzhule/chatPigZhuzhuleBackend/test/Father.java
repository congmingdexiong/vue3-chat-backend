package zhuzhule.chatPigZhuzhuleBackend.test;

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
    Son son = new Son();
    son.printId();
    son.sayHello();
  }
}
