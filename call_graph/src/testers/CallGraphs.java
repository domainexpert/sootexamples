package testers;

public class CallGraphs
{
    static private A field;
    
    public static void main(String[] args) {
	field = new B();
	doStuff();
    }
    
    /**
     * Here soot will declare that B.foo() can be called by doStuff(),
     * where in reality it is not called by doStuff(), showing some
     * imprecision.
     */
    public static void doStuff() {
	new A().foo();
    }
}

class A
{
    public void foo() {
	System.out.println("A.foo() executed.");
	bar();
    }
	
    public void bar() {
	System.out.println("A.bar() executed.");
    }
}

class B extends A {
    @Override
    public void foo() {
	System.out.println("B.foo() executed.");
	bar();
    }
}
