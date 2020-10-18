# JAVA笔记



## 一、Java技术体系

### 1.JDK （Java Development Kit）

​	用于支持Java程序开发的最小环境。（有些地方代指整个Java技术体系）

​	=={ Java程序设计语言、Java虚拟机、Java类库 }==

### 2.JRE（Java Runtime Environment）

​	用于支持Java程序运行的虚拟环境。

​	=={ Java类库API中的Java SE API子集、Java虚拟机 }==

### 3. Java SE（Java Standard Edition）

​	支持面向桌面级应用的Java平台。

​	==完整的Java核心API。==

### 4.Java EE（Java Enterprise Edition）

​	支持使用多层架构的企业应用。

​	==在Java SE API基础上，提供javax.*扩展包。==

------



## 二、JVM



### 1. 运行时数据区域



### 2. HotSpot虚拟机对象



### 3.垃圾回收与内存分配策略



### 4.

------



## 三、Java程序设计语言基础



### 1.类库API



#### 1.1.Class.getResource

getResource是java.lang.Class的方法，就是由字节码对象调用。

**1.1.1classpath，指的是编译后的class文件、xml、properties和txt或其他文件所在的目录**

   * 比如，如果是==maven项目==，classpath为“项目名/target/classes”
   * 如果是普通项目，可能是”项目名/bin”，或者”项目名/build/classes”等等

**1.1.2getResource接受一个字符串参数：**

* 如果以”/”开头，就在classpath根目录下找（==不会递归查找子目录==）

* 如果不以”/”开头，就在调用getResource的字节码对象所在目录下找（==同样不会递归查找子目录==）

**1.1.3相较于Class.getClassLoader.getResource(Srting path)**

* ==path不能以“/”开头==，否则返回null

* path在classpath根目录下找

* class.getResource("/")效果等于getClass().getClassLoader().getResource("")

  ![](D:\Program Files\Typora\java笔记资源\2018061710393350.png)
  
  ```java
      @Test
      public void classPathTest(){
          System.out.println("---------------Class.getClassLoader.getResource");
          System.out.println(this.getClass().getClassLoader().getResource(""));
          System.out.println(this.getClass().getClassLoader().getResource("1.txt"));
          System.out.println(this.getClass().getClassLoader().getResource("NIOTest/1.txt"));
          System.out.println("---------------Class.getResource");
          System.out.println(this.getClass().getResource(""));
          System.out.println(this.getClass().getResource("/"));
          System.out.println(this.getClass().getResource("/1.txt"));
          System.out.println(this.getClass().getResource("1.txt"));
    }
  ```
  
  ```
    ---------------Class.getClassLoader.getResource
    file:/D:/sources/java/JavaStudy/target/test-classes/
    file:/D:/sources/java/JavaStudy/target/test-classes/1.txt
    file:/D:/sources/java/JavaStudy/target/test-classes/NIOTest/1.txt
    ---------------Class.getResource
    file:/D:/sources/java/JavaStudy/target/test-classes/NIOTest/
    file:/D:/sources/java/JavaStudy/target/test-classes/
    file:/D:/sources/java/JavaStudy/target/test-classes/1.txt
    file:/D:/sources/java/JavaStudy/target/test-classes/NIOTest/1.txt
  ```
  
  

#### 1.2. URL与URI

  







------



## 四、高级编程



### 1. 并发编程



#### 1.1  进程与线程

**进程：**

* 程序由指令和数据组成，但这些指令要运行，数据要读写，就必须将指令加载至 CPU，数据加载至内存。在指令运行过程中还需要用到磁盘、网络等设备。进程就是用来加载指令、管理内存、管理 IO 的

* 当一个程序被运行，从磁盘加载这个程序的代码至内存，这时就开启了一个进程。

*  进程就可以视为程序的一个实例。大部分程序可以同时运行多个实例进程（例如记事本、画图、浏览器等），也有的程序只能启动一个实例进程（例如网易云音乐、360 安全卫士等）

**线程：**

* 一个进程之内可以分为一到多个线程。

* 一个线程就是一个指令流，将指令流中的一条条指令以一定的顺序交给 CPU 执行

* ==Java 中，线程作为最小调度单位，进程作为资源分配的最小单位。 在 windows 中进程是不活动的，只是作为线程的容器==

**两者的对比：**

* 进程基本上相互独立的，而线程存在于进程内，是进程的一个子集

* 进程拥有共享的资源，如内存空间，供其内部的线程共享

* 进程间通讯较为复杂：

  - ==同一台计算机上的进程通信叫 IPC（Inter-process communication）==

  - ==不同计算机之间的进程通信，需要通过网络，并蹲守共同的协议，例如HTTP==

* 线程通信相对简单，因为它们共享进程内的内存，一个例子是多个线程可以访问同一个共享变量

* 线程更轻量，线程上下文切换成本一般上要比进程上下文切换低



#### 1.2 并行与并发

引用 Rob Pike 的一段描述：

* 并发（concurrent）是同一时间应对（dealing with）多件事情的能力
* 并行（parallel）是同一时间动手做（doing）多件事情的能力



#### 1.3 Java线程



##### 1.3.1 创建与运行

* 方法一，直接Thread（匿名类）

```java
// 创建线程对象
Thread t = new Thread() {
	public void run() {
	// 要执行的任务
	}
};
// 启动线程
t.start();
```

* 方法二，使用Runnable配合Thread

==把【线程】和【任务】（要执行的代码）分开==

`用 Runnable 更容易与线程池等高级 API 配合`
`用 Runnable 让任务类脱离了 Thread 继承体系，更灵活`

​	Thread 代表线程
​	Runnable 可运行的任务（线程要执行的代码）

```Java
Runnable runnable = new Runnable() {
	public void run(){
	// 要执行的任务
	}
};
// 创建线程对象
Thread t = new Thread( runnable );
// 启动线程
t.start();
```

* 方法三，FutureTask配合Thread

FutureTask ==能够接收 Callable 类型的参数==，用来处理有返回结果的情况

```Java
// 创建任务对象
FutureTask<Integer> task3 = new FutureTask<>(() -> {
	log.debug("hello");
	return 100;
});
// 参数1 是任务对象; 参数2 是线程名字，推荐
new Thread(task3, "t3").start();
// 主线程阻塞，同步等待 task 执行完毕的结果
Integer result = task3.get();
log.debug("结果是:{}", result);
```



##### 1.3.2 ==线程运行原理==

**栈与栈帧**

Java Virtual Machine Stacks （Java 虚拟机栈）

我们都知道 JVM 中由堆、栈、方法区所组成，其中栈内存是给谁用的呢？其实就是线程，每个线程启动后，虚拟机就会为其分配一块栈内存。

* 每个栈由多个栈帧（Frame）组成，对应着每次方法调用时所占用的内存
* 每个线程只能有一个活动栈帧，对应着当前正在执行的那个方法

**线程上下文切换（Thread Context Switch）**

因为以下一些原因导致 cpu 不再执行当前的线程，转而执行另一个线程的代码

* 线程的 cpu 时间片用完
* 垃圾回收
* 有更高优先级的线程需要运行
* 线程自己调用了 sleep、yield、wait、join、park、synchronized、lock 等方法

当 Context Switch 发生时，需要由操作系统保存当前线程的状态，并恢复另一个线程的状态，Java 中对应的概念就是程序计数器（Program Counter Register），它的作用是记住下一条 jvm 指令的执行地址，是线程私有的状态包括程序计数器、虚拟机栈中每个栈帧的信息，如局部变量、操作数栈、返回地址等
Context Switch 频繁发生会影响性能。

<img src="D:\Program Files\Typora\java笔记资源\微信图片_20201006151224.png" style="zoom: 80%;" />



##### 1.3.3 线程的常见方法表

| 方法名                                  | static | 功能说明                                                     | 注意                                                         |
| --------------------------------------- | :----- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| start()                                 |        | 启动一个新线<br/>程，在新的线程运行 run <br/>方法中的代码    | start 方法只是让线程进入就绪，里面代码不一定立刻<br/>运行（CPU 的时间片还没分给它）。每个线程对象的<br/>start方法只能调用一次，如果调用了多次会出现<br/>IllegalThreadStateException |
| run()                                   |        | 新线程启动后会<br/>调用的方法                                | 如果在构造 Thread 对象时传递了 Runnable 参数，则<br/>线程启动后会调用 Runnable 中的 run 方法，否则默<br/>认不执行任何操作。但可以创建 Thread 的子类对象，<br/>来覆盖默认行为 |
| join()                                  |        | 等待线程运行结<br/>束                                        | t1.join()用在t1.start()后                                    |
| join( long n)                           |        | 等待线程运行结<br/>束,最多等待 n<br/>毫秒                    |                                                              |
| getId()                                 |        | 获取线程长整型<br/>的 id                                     | id 唯一                                                      |
| getName()&setName(String name)          |        | 获取线程名&修改线程名                                        |                                                              |
| getPriority()&setPriority(int priority) |        | 获取线程优先级&修改线程优先级                                | java中规定线程优先级是1~10 的整数，较大的优先级<br/>能提高该线程被 CPU 调度的机率 |
| getState()                              |        | 获取线程状态                                                 | Java 中线程状态是用 6 个 enum 表示，分别为：<br/>NEW, RUNNABLE, BLOCKED, WAITING,<br/>TIMED_WAITING, TERMINATED |
| isInterrupted()                         |        | 判断是否被打<br/>断                                          | 不会清除==打断标记==                                         |
| isAlive()                               |        | 线程是否存活<br/>（还没有运行完<br/>毕）                     |                                                              |
| interrupt()                             |        | 打断线程                                                     | 如果被打断线程正在 sleep，wait，join 会导致被打断<br/>的线程抛出 InterruptedException，并==清除==打断标<br/>记 ；如果打断的正在运行的线程，则会==设置==打断标<br/>记 ；park 的线程被打断，也会==设置==打断标记 |
| interrupted()                           | yes    | 判断当前线程是<br/>否被打断                                  | 用途是清除==打断标记==                                       |
| currentThread()                         | yes    | 获取当前正在执<br/>行的线程                                  |                                                              |
| sleep(long n)                           | yes    | 让当前执行的线<br/>程休眠n毫秒，<br/>休眠时让出 cpu<br/>的时间片给其它<br/>线程 |                                                              |
| yield()                                 | yes    | 提示线程调度器<br/>让出当前线程对<br/>CPU的使用              | 主要是为了测试和调试                                         |

不推荐stop(),suspend(),resume()



##### 1.3.4 interrupt方法详解

* **打断sleep, wait, join的线程**

  这几个方法都会让线程进入阻塞状态

  打断sleep线程，会清空打断状态，以sleep为例

```java
private static void test1() throws InterruptedException {
	Thread t1 = new Thread(()->{
		sleep(1);
	}, "t1");
	t1.start();
	sleep(0.5);
	t1.interrupt();
	log.debug(" 打断状态: {}", t1.isInterrupted());
}
//////////
java.lang.InterruptedException: sleep interrupted
	at java.lang.Thread.sleep(Native Method)
	at java.lang.Thread.sleep(Thread.java:340)
	at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
	at cn.itcast.n2.util.Sleeper.sleep(Sleeper.java:8)
	at cn.itcast.n4.TestInterrupt.lambda$test1$3(TestInterrupt.java:59)
	at java.lang.Thread.run(Thread.java:745)
21:18:10.374 [main] c.TestInterrupt - 打断状态: false
```

* **打断正常运行的线程**

  打断正常运行的线程，不会清空打断状态

  ```java
  private static void test2() throws InterruptedException {
  	Thread t2 = new Thread(()->{
  		while(true) {
  			Thread current = Thread.currentThread();
  			boolean interrupted = current.isInterrupted();
  			if(interrupted) {
  			log.debug(" 打断状态: {}", interrupted);
  			break;
  			}
  		}
  	}, "t2");
  	t2.start();
  	sleep(0.5);
  	t2.interrupt();
  }
  //////
  20:57:37.964 [t2] c.TestInterrupt - 打断状态: true
  ```



##### 1.3.5 ==两阶段的终止==

在一个线程中如何“优雅”终止另外一个线程，即是给被终止的线程留下时间去处理后事。

**错误思路**

* 使用线程对象的stop()方法停止线程
  - stop方法会真正杀死线程，如果这时被杀死的线程掌握了共享资源的锁，那么它被杀死后就再也没有机会去释放锁，==使其它线程无法在获得锁==。

* 使用System.exit(int)方法停用线程
  - 目的是停止线程，但是却把整个程序都停止了。

**两阶段终止模式**



<img src="D:\Program Files\Typora\java笔记资源\两阶段终止.PNG" style="zoom:67%;" />

* **利用isInterrupted**

interrupt可以打断正在执行的线程，无论这个线程实在sleep, wait, 还是在正常运行

```java
class TPTInterrupt {
	private Thread thread;
    
	public void start(){
		thread = new Thread(() -> {
			while(true) {
				Thread current = Thread.currentThread();
				if(current.isInterrupted()) {
                    //
					log.debug("料理后事");
                    //
					break;
				}
				try {
                    //睡眠2s，防止线程占用过多的cpu资源
					Thread.sleep(2000);
					log.debug("将结果保存");
				} catch (InterruptedException e) {
					current.interrupt();
                }
				// 执行监控操作
			}
		},"监控线程");
		thread.start();
	}
    
	public void stop() {
        //“优雅”终止thread线程
		thread.interrupt();
	}
}
```

调用

```java
TPTInterrupt t = new TPTInterrupt();
t.start();

Thread.sleep(3500);

log.debug("stop");
t.stop();
```

结果

```java
11:49:42.915 c.TwoPhaseTermination [监控线程] - 将结果保存
11:49:43.919 c.TwoPhaseTermination [监控线程] - 将结果保存
11:49:44.919 c.TwoPhaseTermination [监控线程] - 将结果保存
11:49:45.413 c.TestTwoPhaseTermination [main] - stop
11:49:45.413 c.TwoPhaseTermination [监控线程] - 料理后事
```

* **利用停止标志**

```java
// 停止标记用 volatile 是为了保证该变量在多个线程之间的可见性
// 我们的例子中，即主线程把它修改为 true 对 t1 线程可见
class TPTVolatile {
    
	private Thread thread;
	private volatile boolean stop = false;
    
	public void start(){
		thread = new Thread(() -> {
			while(true) {
				Thread current = Thread.currentThread();
				if(stop) {
					log.debug("料理后事");
					break;
				}
				try {
					Thread.sleep(1000);
					log.debug("将结果保存");
				} catch (InterruptedException e) {

				}
				// 执行监控操作
			}
		},"监控线程");
		thread.start();
	}
	public void stop() {
		stop = true;
		thread.interrupt();
	}
}
```

调用

```java
TPTVolatile t = new TPTVolatile();
t.start();

Thread.sleep(3500);
log.debug("stop");
t.stop();
```

结果

```java
11:54:52.003 c.TPTVolatile [监控线程] - 将结果保存
11:54:53.006 c.TPTVolatile [监控线程] - 将结果保存
11:54:54.007 c.TPTVolatile [监控线程] - 将结果保存
11:54:54.502 c.TestTwoPhaseTermination [main] - stop
11:54:54.502 c.TPTVolatile [监控线程] - 料理后事
```



##### 1.3.6 主线程与守护线程

默认情况下，Java 进程需要等待所有线程都运行结束，才会结束。有一种特殊的线程叫做守护线程，只要其它非守
护线程运行结束了，==即使守护线程的代码没有执行完，也会强制结束==。

```java 
log.debug("开始运行...");
Thread t1 = new Thread(() -> {
	log.debug("开始运行...");
	sleep(2);
	log.debug("运行结束...");
}, "daemon");
// 设置该线程为守护线程
t1.setDaemon(true);
t1.start();

sleep(1);
log.debug("运行结束...");
/*
 * 主线程运行结束，但是没有等待t1运行完（打印[daemon] c.TestDaemon - 运行结束...），直接结束整个程序
 */
//////
08:26:38.123 [main] c.TestDaemon - 开始运行...
08:26:38.213 [daemon] c.TestDaemon - 开始运行...
08:26:39.215 [main] c.TestDaemon - 运行结束...
```

>注意
>
>* 垃圾回收器线程就是一种守护线程
>* Tomcat 中的 Acceptor 和 Poller 线程都是守护线程，所以 Tomcat 接收到 shutdown 命令后，不会等
>  待它们处理完当前请求



##### 1.3.7 Java线程的六种状态

<img src="D:\Program Files\Typora\java笔记资源\六种状态.PNG" style="zoom: 67%;" />

*  NEW 线程刚刚被创建出来，但是还没有调用start()启动方法
*  RUNNABLE 当调用start()方法之后，注意，Java API 层面的 RUNNABLE 状态涵盖了操作系统层面的
  【可运行状态】、【运行状态】和【阻塞状态】（由于 BIO 导致的线程阻塞，在 Java 里无法区分，仍然认为
  是可运行）
* BLOCKED ， WAITING ， TIMED_WAITING 都是 Java API 层面对【阻塞状态】的细分
* TERMINATED 当线程代码运行结束



#### 1.4 共享模式之管程



**临界区 Critical Section**

* 一个程序运行多个线程本身是没有问题的
* 问题出在多个线程访问**共享资源**
  - 多个线程读**共享资源**其实也没有问题
  - 在多个线程对共享资源读写操作时发生指令交错，就会出现问题
* 一段代码块内如果存在对**共享资源**的多线程读写操作，称这段代码块为**临界区**



**竞态条件 Race Condition**

多个线程在临界区内执行，由于代码的**执行序列**不同而导致结果无法预测，称之为发生了**竞态条件**



##### 1.4.1 变量的线程安全分析

* 成员变量和静态变量
  - 如果他们没有共享，则线程安全
  - 如果共享了：
    - 只有读操作，线程安全
    - 读写操作，则出现临界区，线程不安全

* 局部变量

  - 局部变量是线程安全的

  ```java
  	public static void test1() {
  		int i = 10;
  		i++;
  	}
  ```

  <img src="D:\Program Files\Typora\java笔记资源\成员变量.png" style="zoom: 50%;" />

  - 局部变量引用的对象则需要考虑方法的作用区（引用一致，线程不安全）

  ```java 
  	class ThreadUnsafe {
  		ArrayList<String> list = new ArrayList<>();
  		public void method1(int loopNumber) {
  			for (int i = 0; i < loopNumber; i++) {
  			// { 临界区, 会产生竞态条件
  			method2();
  			method3();
              // } 临界区
  			}
  		}
  		private void method2() {
  			list.add("1");
  		}
  		private void method3() {
  			list.remove(0);
  		}
          
          static final int THREAD_NUMBER = 2;
  		static final int LOOP_NUMBER = 200;
          
  		public static void main(String[] args) {
  			ThreadUnsafe test = new ThreadUnsafe();
  			for (int i = 0; i < THREAD_NUMBER; i++) {
  				new Thread(() -> {
  					test.method1(LOOP_NUMBER);
  				}, "Thread" + i).start();
  			}
  		}
          
      }
  /*
  分析：
  	无论哪个线程中的 method2 引用的都是同一个对象中的 list 成员变量
  	method3 与 method2 分析相同
  */
  ```

  <img src="D:\Program Files\Typora\java笔记资源\成员变量的引用.png" style="zoom: 50%;" />

  

##### 1.4.2 常见线程安全类

| 类                   | 备注                                               |
| -------------------- | -------------------------------------------------- |
| Integer              | 不可变类                                           |
| String               | 不可变类，replace，substring方法返回新的String对象 |
| StringBuffer         | StringBuilder不是                                  |
| Random               |                                                    |
| Vector               |                                                    |
| Hashtable            |                                                    |
| java.util.concurrent |                                                    |

> 注意，只是类的方法线程安全、原子性；
>
> 但多个方法的组合不是原子性：
>
> if( table.get("key") == null) {
> 	table.put("key", value);
> }



##### 1.4.3 ==Monitor原理==

java对象头中的**Mark Word**结构：

```Ruby
|-------------------------------------------------------------------------------------|--------------------|
|                                Mark Word (64 bits)                                  |        State       |
|-------------------------------------------------------------------------------------|--------------------|
| unused:25 bits | hashcode:31 bits | unused:1 bit | age:4 bits | biased_lock:0 | 01  |        Normal      |
|-------------------------------------------------------------------------------------|--------------------|
| thread:54 bits | epoch:2 bits     | unused:1 bit | age:4 bits | biased_lock:1 | 01  |        Biased      |
|-------------------------------------------------------------------------------------|--------------------|
|                        ptr_to_lock_record:62 bits                             | 00  | Lightweight Locked |
|-------------------------------------------------------------------------------------|--------------------|
|                        ptr_to_heavyweight_monitor:62 bits                     | 10  | Heavyweight Locked |
|-------------------------------------------------------------------------------------|--------------------|
|                                                                               | 11  |    Marked for GC   |
|-------------------------------------------------------------------------------------|--------------------|
```



每个 Java 对象都可以关联一个 Monitor 对象，（指针指向）

如果使用 synchronized 给对象上锁（**重量级**）之后，该对象头的Mark Word 中就被设置指向 Monitor 对象的指针（ptr_to_heavyweight_monitor），

Monitor由操作系统提供。



<img src="D:\Program Files\Typora\java笔记资源\Monitor.png" style="zoom: 67%;" />



**Monitor步骤规则：**

- 刚开始 Monitor 中的 Owner 为 null 
- 当 Thread-2 执行synchronized(obj) 就会将 Monitor 的所有者 Owner 置为 Thread-2，Monitor中只能有一个 Owner
- 在 Thread-2 上锁的过程中，如果 Thread-3，Thread-4，Thread-5 也来执行 synchronized(obj)，就会进入 EntryList BLOCKED
- Thread-2 执行完同步代码块的内容，然后唤醒 EntryList 中等待的线程来竞争锁，竞争的时是非公平的
- 图中 WaitSet 中的 Thread-0，Thread-1 是之前获得过锁，但条件不满足进入 WAITING 状态的线程

> 注意：
>
> - synchronized 必须是进入同一个对象的 monitor 才有上述的效果
> - 不加 synchronized 的对象不会关联监视器，不遵从以上规则



##### 1.4.4 ==synchronized原理==

###### **(0) 重量级锁**

* 每次请求锁时，向lock所指向的 Monitor 查询 Owner 是否为 null ，null 则线程给 Monitor 上锁，否则进入 EntryList 阻塞 BLOCKED 。

* 对程序员透明，jvm实现。

在字节码中的体现：

```java
	static final Object lock = new Object();
	static int counter = 0;
	public static void main(String[] args) {
		synchronized (lock) {
			counter++;
		}
	}

//对应字节码内容
public static void main(java.lang.String[]);
	descriptor: ([Ljava/lang/String;)V
	flags: ACC_PUBLIC, ACC_STATIC
    Code:
        stack=2, locals=3, args_size=1
    		0: getstatic #2 // <- lock引用 （synchronized开始）
			3: dup
			4: astore_1 // lock引用 -> slot 1
         /----------------------------/
         //  重量级锁
         /----------------------------/
			5: monitorenter // 将 lock对象 MarkWord 置为 Monitor 指针
			6: getstatic #3 // <- i
			9: iconst_1 // 准备常数 1
			10: iadd // +1
			11: putstatic #3 // -> i
			14: aload_1 // <- lock引用
			15: monitorexit // 将 lock对象 MarkWord 重置, 唤醒 EntryList
			16: goto 24
         /----------------------------/
         //  6-16发生异常的话，在19-22的异常处理中会把lock的锁释放，monitorexit
         /----------------------------/
			19: astore_2 // e -> slot 2
			20: aload_1 // <- lock引用
			21: monitorexit // 将 lock对象 MarkWord 重置, 唤醒 EntryList
			22: aload_2 // <- slot 2 (e)
			23: athrow // throw e
			24: return
		Exception table:
			from to target type
			   6 16     19 any
			  19 22     19 any
		LineNumberTable:
			line 8: 0
			line 9: 6
			line 10: 14
			line 11: 24
		LocalVariableTable:
			Start  Length  Slot  Name  Signature
				0      25     0  args  [Ljava/lang/String;
		StackMapTable: number_of_entries = 2
			frame_type = 255 /* full_frame */
				offset_delta = 19
				locals = [ class "[Ljava/lang/String;", class java/lang/Object ]
				stack = [ class java/lang/Throwable ]
			frame_type = 250 /* chop */
				offset_delta = 4
```

> **注意**
>
> 方法级别的 synchronized 不会在字节码指令中有所体现



###### (1) 轻量级锁

* 轻量级锁的使用场景：如果一个对象虽然有多线程要加锁，但加锁的时间是错开的（也就是没有竞争），那么可以使用轻量级锁来优化。

* 轻量级锁对使用者是透明的，即语法仍然是 synchronized

假设有两个方法同步块，利用同一个对象加锁：

```java 
	static final Object obj = new Object();
	public static void method1() {
		synchronized( obj ) {
			// 同步块 A
			method2();
		}
	}
	public static void method2() {
		synchronized( obj ) {
			// 同步块 B
		}
	}
```

- 创建锁记录（Lock Record）对象，每个线程中的栈帧都会包含一个锁记录的结构，内部可以存储锁定对象的 Mark Word

<img src="D:\Program Files\Typora\java笔记资源\轻量级锁1.png" style="zoom: 50%;" />

- 让锁记录中 Object reference 指向锁对象，并尝试用 cas 替换 Object 的 Mark Word，将 Mark Word 的值存入锁记录

<img src="D:\Program Files\Typora\java笔记资源\轻量级锁2.png" style="zoom: 50%;" />

- 如果 cas 替换成功，对象头中存储了 锁记录地址和状态 00 ，表示由该线程给对象加锁，这时图示如下

<img src="D:\Program Files\Typora\java笔记资源\轻量级锁3.png" style="zoom: 50%;" />

- 如果 cas 失败，有两种情况
  - 如果是其它线程已经持有了该 Object 的轻量级锁，这时表明有竞争，进入锁膨胀过程
  - 如果是自己执行了 synchronized 锁重入，那么再添加一条 Lock Record （锁记录为null）作为重入的计数

<img src="D:\Program Files\Typora\java笔记资源\轻量级锁4.png" style="zoom: 50%;" />

- 当退出 synchronized 代码块（解锁时）如果有取值为 null 的锁记录，表示有重入，这时重置锁记录，表示重入计数减一

<img src="D:\Program Files\Typora\java笔记资源\轻量级锁5.png" style="zoom: 50%;" />

- 当退出 synchronized 代码块（解锁时）锁记录的值不为 null，这时使用 cas 将 Mark Word 的值恢复给对象头
  - 成功，则解锁成功
  - 失败，说明轻量级锁进行了锁膨胀或已经升级为重量级锁，进入重量级锁解锁流程



###### (2) 锁膨胀

如果在尝试加轻量级锁的过程中，CAS 操作无法成功，这时一种情况就是有其它线程为此对象加上了轻量级锁（**有竞争**），这时需要进行锁膨胀，将轻量级锁变为**重量级锁**。

```java
	static Object obj = new Object();
	public static void method1() {
		synchronized( obj ) {
			// 同步块
		}
	}
```

- 当 Thread-1 进行轻量级加锁时，Thread-0 已经对该对象加了轻量级锁

<img src="D:\Program Files\Typora\java笔记资源\锁膨胀1.png" style="zoom: 73%;" />

- 这时 Thread-1 加轻量级锁失败，进入锁膨胀流程
  - 即为 Object 对象申请 Monitor 锁，让 Object 指向重量级锁地址
  - 然后自己进入 Monitor 的 EntryList BLOCKED

<img src="D:\Program Files\Typora\java笔记资源\锁膨胀2.png" style="zoom:73%;" />



###### (3) 自旋优化

重量级锁竞争的时候，还可以使用自旋来进行优化，如果当前线程自旋成功（即这时候持锁线程已经退出了同步块，释放了锁），这时当前线程就可以避免阻塞。

- 自旋会占用 CPU 时间，单核 CPU 自旋就是浪费，多核 CPU 自旋才能发挥优势。

- 在 Java 6 之后自旋锁是自适应的，比如对象刚刚的一次自旋操作成功过，那么认为这次自旋成功的可能性会高，就多自旋几次；反之，就少自旋甚至不自旋，总之，比较智能。
- Java 7 之后不能控制是否开启自旋功能



###### (4) 偏向锁



###### (5) 锁消除





### 2. NIO

从1.4版本开始，引进新的异步IO库，面向缓冲区的IO，被称为New IO类库，简称JAVA NIO。



#### 2.1 Channel

Channel和IO中的Stream(流)是差不多一个等级的。

只不过Stream是单向的，譬如：InputStream, OutputStream.而Channel是双向的，既可以用来进行读操作，又可以用来进行写操作。
NIO中的Channel的主要实现有：

##### 2.1.1 FileChannel

==FileChannel是一个用读写，映射和操作一个文件的通道。==

- 打开FileChannel

```Java
FileChannel fileChannel = FileChannel.open(Path.of(url),Set<? extends OpenOption> openOptions);

//模式“r”表示通道仅为“只读“，注意，关闭RandomAccessFile也将关闭与之关联的通道。
RandomAccessFile reader = new RandomAccessFile(file, "r");
FileChannel fileChannel = reader.getChannel();
//使用FileInputStream打开一个FileChannel来读取文件。同样的，关闭FileInputStream也会关闭与之相关的通道。
FileInputStream fin= new FileInputStream(file);
FileChannel fileChannel = fin.getChannel();
```

- 读写操作

```Java
/*
从channel中读取到buffer
*/
ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//读取1024字节内容到byteBuffer中
while(fileChannel.read(byteBuffer) > 0){
    //需要使用字符集(Charset)将字节数组解码为字符串
	String fileContent = new String(byteBuffer.array(), StandardCharsets.UTF_8);
    /*
    do something
    */
}
/*
从buffer中写入到channel
*/
byteBuffer.clear();
byteBuffer.put("需要写入的数据".getBytes(StandardCharsets.UTF_8));
//类似于flush()函数功能，将buffer里面的数据刷新出去
byteBuffer.flip();
//检查是否还有数据未写入
while (byteBuffer.hasRemaining()) {
    fileChannel.write(byteBuffer,0);
}
```

- 关闭通道

```java 
fileChannel.close();
```

- 获取与设置当前位置（字节）

```Java
//get
long originalPosition = fileChannel.position();
//set
fileChannel.position(5);
```

- 获取文件大小

```java
fileChannel.size();
```

- 除了读写操作之外，还有裁剪特定大小文件truncate()，强制在内存中的数据刷新到硬盘中去force()，对通道上锁lock()等功能。

```java
//方法将文件截断为给定的大小(以字节为单位),指定长度后面的部分将被删除。
fielChannel = fileChannel.truncate(5);
/*
强制文件内容和元数据不断写入磁盘,仅当文件存储在本地设备上时，才能保证该方法有效。
因为系统会将数据先保存在内存中，不保证数据会立即写入到硬盘中，所以有这个需求，就可以直接强制数据写入内存中。
*/
fileChannel.force(true);
/*
阻止对文件某一部分进行高并发访问。

tryLock方法尝试获取文件部分(file section)上的锁。
如果请求的文件部分已被另一个线程阻塞，它将抛出一个OverlappingFileLockException异常。
此方法还接受Boolean参数来请求共享锁或独占锁。
我们应该注意到，有些操作系统可能不允许共享锁，默认情况下是独占锁。
*/
FileLock fileLock = fileChannel.tryLock(6, 5, Boolean.FALSE);
```



##### 2.1.2 DatagramChannel

==DatagramChannel是一个能收发UDP包的通道。==

- 打开DatagramChannel

```java
/*
打开的DatagramChannel可以在UDP端口8989上接收数据包。
*/
DatagramChannel channel = DatagramChannel.open(); // 获取通道
channel.bind(new InetSocketAddress(8989)); // 绑定端口
```

- 接受与发送数据

```java
//接受并读取打印
SocketAddress socketAddress = channel.receive(buffer);
if (socketAddress != null) {
	int position = buffer.position();
	b = new byte[position];
	buffer.flip();
	for (int i = 0; i < position; ++i) {
		b[i] = buffer.get();
    }
	System.out.println("receive remote " + socketAddress.toString() + ":" + new String(b, StandardCharsets.UTF_8));
}
//发送
int bytesSend = channel.send(buffer, socketAddress);
```

- 连接到特定的地址

  可以将DatagramChannel"连接"到网络中的特定地址的。

  由于UDP是无连接的，连接到特定地址并不会像TCP通道那样创建一个真正的连接。而是锁住DatagramChannel，让其只能从特定地址收发数据。

```java
channel.connect(new InetSocketAddress(ip, 80));
//当连接后，也可以使用read()和write()方法，就像在用传统的通道一样。只是在数据传送方面没有任何保证。
int bytesRead = channel.read(buffer);
int bytesWritten = channel.write(buffer);
```

##### 2.1.3 SocketChannel & ServerSocketChannel

这两个是网络连接的通道，SocketChannel负责连接传输，ServerSocketChannel负责连接的监听。

| NIO                 | OIO          |
| ------------------- | ------------ |
| SocketChannel       | Socket       |
| ServerSocketChannel | ServerSocket |

![](D:\Program Files\Typora\java笔记资源\ssc.PNG)

> ServerSocketChannel位于服务器端；
>
> SocketChannel同时位于服务器和客户端，两者负责传输。

第一步：获取SocketChannel传输通道

在==客户端==，先通过SocketChannel静态方法open()获得一个套接字传输通道；接着Channel设置为非阻塞模式，最后通过connect()实例方法，对服务器的IP和端口发起连接。

```java
	SocketChannel socketChannel = SocketChannel.open();
	socketChannel.configureBlocking(false);
	socketChannel.connect(new InetSocketAddress("127.0.0.1",80));
	//由于非阻塞模式下connect可能与服务器的连接还没有得到真正的建立，connect方法就返回结果了，因此要不断自旋，检查是否连接到了服务器主机。
	while(! socketChannel.finishConnect()){
        //do something
    }
```

在==服务器端==，ServerSocketChannel查询到一个OP_ACCEPT就绪事件后，可以通过调用服务器端的ServerSocketChannel.accept()方法，来获取套接字传输通道。

```java
	//key为SelectionKey选择关键字的OP_ACCEPT
	ServerSocketChannel server = (ServerSocketChannel) key.channel();
	SocketChannel socketChannel = server.accept();
	socketChannel.configureBlocking(false);
```

第二步：读取SocketChannel传输通道

```java
	ByteBuffer buf = ByteBuffer.allocate(1024);
	long bytesRead = socketChannel.read(buf);
	while(bytesRead > 0){
        buf.flip();
        while(buf.hasRemaining()){
            //
        }
        buf.clear();
        bytesRead = socketChannel.read(buf);
    }
	//-1 表示读取到对方的结束标记，准备关闭连接。
	if(bytesRead == -1){
        socketChannel.close();
    }
```

第三步：写入到SocketChannel传输通道

```java
	ByteBuffer buf = (ByteBuffer) key.attachment();
	buf.flip();
	socketChannel.write(buf);
	buf.compact();
```

第四步：关闭SocketChannel传输通道

```java
	//向对方发送一个输出的结束标记（-1）。
	socketChannel.shutdownOutput();
	socketChannel.close();
```



#### 2.2 Buffer

NIO中的关键Buffer实现有：ByteBuffer, CharBuffer, DoubleBuffer, FloatBuffer, IntBuffer, LongBuffer, ShortBuffer，分别对应基本数据类型: byte, char, double, float, int, long, short。

当然NIO中还有MappedByteBuffer, HeapByteBuffer, DirectByteBuffer。

**变量**

| 索引     | 说明                                                    |
| -------- | :------------------------------------------------------ |
| capacity | 缓冲区数组的总长度                                      |
| position | 下一个要操作的数据元素的位置                            |
| limit    | 缓冲区数组中不可操作的下一个元素的位置：limit<=capacity |
| mark     | 用于记录当前position的前一个位置或者默认是-1            |



**使用buffer的基本步骤：**

① **分配空间**，ByteBuffer buffer = ByteBuffer.allocate(int size); （还有一种是allocateDirector）

  ![](D:\Program Files\Typora\java笔记资源\捕获.PNG)

② **写入数据**到buffer，int bytesRead = channel.read(buffer);

  ![](D:\Program Files\Typora\java笔记资源\捕获1.PNG)

③ **调用filp()方法**为读准备，buffer.flip();

  ```java
  /* Flips this buffer.
   * The limit is set to the current position and then the position is set to zero.
   * If the mark is defined then it is discarded.
   */
  public Buffer flip() {
  	limit = position;
  	position = 0;
  	mark = -1;
  	return this;
  }
  ```

  ![](D:\Program Files\Typora\java笔记资源\捕获2.PNG)

④ 从buffer中**读取数据**，buffer.get();

  ![](D:\Program Files\Typora\java笔记资源\捕获3.PNG)

⑤ 调用clear()方法（清除）或==compact()方法（未读压缩）==

  ![](D:\Program Files\Typora\java笔记资源\捕获4.PNG)

  ![](D:\Program Files\Typora\java笔记资源\捕获5.PNG)

  - 相同点：调用完compcat和clear方法之后的buffer对象一般都是继续往该buffer中写入数据的。

  - 不同点：

    - clear是把position=0，limit=capcity等，数据将“被遗忘”，也就是说，除了内部数组，其他属性都还原到buffer创建时的初始值，而内部数组的数据虽然没赋为null，但只要不在clear之后误用buffer.get就不会有问题，正确用法是使用buffer.put从头开始写入数据;

    - 而compcat是把buffer中内部数组剩余未读取的数据复制到该数组从索引为0开始，然后position设置为复制剩余数据后的最后一位元素的索引+1，limit设置为capcity，此时在0 ~ position之间是未读数据，而position ~ limit之间是buffer的剩余空间，可以put数据。

  ```java
  
  buf.clear();          // Prepare buffer for use
  while (in.read(buf) >= 0 || buf.position != 0) {
  	buf.flip();
  	out.write(buf);   // 非阻塞 io, buf可能还没有写完。
  	buf.compact();    // In case of partial write
  }
  /*
   * channel 是一种非阻塞 io 操作，write操作并不能一次将buffer 中的数据全部写入到指定的 channel 中去。
   * 该方法的作用是将 position 与 limit之间的数据复制到buffer的开始位置，复制后 position  = limit -position,limit = capacity
   * 但如果position 与limit 之间没有数据的话发，就不会进行复制。
   */
  
  
  //源码
  public ByteBuffer compact() {
      
      int pos = position();
      int rem = limit() - pos;
      
    	//buffer内部数组的position~limit之间的字节移动到buffer内部数组的最开始位置。。
    	//比如当前position索引位置的字节移到0位置，position+1的字节移动到1位置，以此类推，直到limit位置的移动完位置。
    	System.arraycopy(hb, ix(pos), hb, ix(0), rem);
            
    	//当前position设置为剩余字节数（limit-position），即在剩余字节的最后一个字节的下一个索引。
    	position(rem);
     
    	//limit设置为当前buffer创建时的容量capacity（内部数据的长度）
    	limit(capacity());
    	discardMark();
      return this; 
  }
  ```

> 其他方法：
>
> * Buffer.mark()，可以标记Buffer中的一个特定的position
> * Buffer.reset()，恢复到mark的position
> * Buffer.rewind()，将position设回0，所以你可以重读Buffer中的所有数据



#### 2.3 ==Selector==

* 选择器的作用是完成IO的多路复用。
* 一个channel代表一条连接通路，通过选择器可以同时监控多个通道的IO状态。
* 选择器和通道的关系，是监控和被监控的关系。
* 在主线程之外开辟一条线程专门处理一个选择器，来监控成千上万的已注册的通道的状态（查询过程是阻塞同步的），这样减少线程之间的上下文切换所导致的开销。

**事件类型：**（表示通道具备某个操作的条件，并不是指对通道的事件操作）

* 可读：SelectionKey.==OP_READ== (public static final int OP_READ = 1 << 0) (00001)
* 可写：SelectionKey.==OP_WRITE== (public static final int OP_WRITE = 1 << 2) (00100)
* 连接：SelectionKey.==OP_CONNECT== (public static final int OP_CONNECT = 1 << 3) (01000)
* 接收：SelectionKey.==OP_ACCEPT== (public static final int OP_ACCEPT = 1 << 4) (10000)

> 如果要监控通道的多种事件，可以用“按位或”运算符来实现
>
> //同时监控读写事件
>
> int key = SelectionKey.OP_READ | SelectionKey.OP_WRITE ;

**SelectionKey选择键：**

SelectionKey选择键就是那些被选择器选中的IO事件。

一个IO事件的发生（就绪态的达成）后，如果之前在选择器中注册过，就会被选择器选中，并放入SelectionKey选择键集合（返回类型Set）中；

但如果之前没有注册过，即使发生了IO事件，也不会被选择器选中。

另外，SelectionKey不仅仅可以获得通道的IO事件类型，还可以获得发生IO事件所在的通道，更可以获得选择键的选择器实例。

```java
SelectionKey.java
	/**
     * Returns the channel for which this key was created.  This method will
     * continue to return the channel even after the key is cancelled.
     *
     * @return  This key's channel
     */
    public abstract SelectableChannel channel();
	/**
     * Returns the selector for which this key was created.  This method will
     * continue to return the selector even after the key is cancelled.
     *
     * @return  This key's selector
     */
    public abstract Selector selector();
```

**使用流程：**

第一步：获取选择器实例（通过默认的SelectorProvider对象，获取一个新的选择器实例）

```java
	Selector selector = Selector.open();
	/**
     * Opens a selector.
     *
     * <p> The new selector is created by invoking the {@link
     * java.nio.channels.spi.SelectorProvider#openSelector openSelector} method
     * of the system-wide default {@link
     * java.nio.channels.spi.SelectorProvider} object.  </p>
     *
     * @return  A new selector
     *
     * @throws  IOException
     *          If an I/O error occurs
     */
    public static Selector open() throws IOException {
        return SelectorProvider.provider().openSelector();
    }
```

第二步：将通道注册到选择器实例

```java
	//获取通道
	ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
	//设置为非阻塞
	serverSocketChannel.configureBlocking(false);
	//绑定连接
	serverSocketChannel.bind(new InetSocketAddress(System.SOCKET_SERVER_PORT));
	//将通道注册到选择器上，并定制监听事件为：接收连接
	serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
```

> 注册到选择器的通道，必须处于非阻塞模式下，否则排除IllegalBlockingModeException异常。
>
> FileChannel不能切换非阻塞模式，他只能在阻塞情况下工作。意味着FileChannel文件通道不能与选择器一起工作。
>
>  
>
> 服务器监听通道ServerSocketChannel仅仅支持ACCEPT的IO事件；而SocketChannel传输通道，则不支持ACCEPT的IO事件。
>
> 可以通过通道的validOps()方法，来获得该通道的所有支持通道。

第三步：选出感兴趣的IO就绪事件（选择键集合）

通过select()方法，选出==已经注册的而且已经就绪==的IO事件，保存到SelectionKey选择键集合Set<SelectionKe>中。

Set保存在Selector实例中。调用selectedKey()方法，可以获得选择键集合。

```java
	while(true) {
		//阻塞监听
		if(selector.select(TIMEOUT) == 0) {
            continue;
        }
		//接受事件
        Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
        //分配处理逻辑，可以启用线程池处理相同的事件，多级事件队列
        while(iter.hasNext()){
            SelectionKey key = iter.next();
            if(key.isAcceptable()) {
                //IO事件：ServerSocketChannel服务器监听通道有新连接
            	handleAccept(key);
            }
            if(key.isReadable()) {
                //IO事件：传输通道可读
            	handleRead(key);
            }
            if(key.isWritable() && key.isValid()) {
                //IO事件：传输通道可写
            	handleWrite(key);
            }
            if(key.isConnectable()) {
                //IO事件：传输通道连接成功
            	System.out.println("isConnectable = true");
            }
            //处理完成后，移除选择键，不然一直循环重复处理同一个。
            iter.remove();
		}
	}
```

```java
	//non-blocking	
    public abstract int selectNow() throws IOException;
    //blocking timeout
    public abstract int select(long timeout) throws IOException;
    //blocking
    public abstract int select() throws IOException;

////
	//返回的整数代表，上一次select到这次select之间，有多少个通道发生了IO事件
```

------



## 五、设计模式



### 1.单例模式

**介绍：**

* 单例模式（Singleton Pattern）是 Java 中最简单的设计模式之一。这种类型的设计模式属于==创建型模式==，它提供了一种创建对象的最佳方式。
* 这种模式涉及到一个单一的类，**该类负责创建自己的对象**，同时确保只有单个对象被创建。这个类提供了一种==访问其唯一的对象的方式==，可以直接访问，不需要实例化该类的对象。
* 销毁，或者控制实例的数目，节省系统资源。

**注意：**

- 单例类只能有一个实例。
- 单例类必须自己创建自己的唯一实例。
- 单例类必须给所有其他对象提供这一实例。

**解决：**

* 提供全局访问点，一个类仅有一个实例。

* 判断系统是否已经有这个单例，如果有则返回，如果没有则创建。不需要提前创建浪费内存资源。

* 构造函数是私有的，防止再创建多的实例。

**使用场景：**

  - 要求生产唯一序列号。
  - WEB 中的计数器，不用每次刷新都在数据库里加一次，用单例先缓存起来。
  - 创建的一个对象需要消耗的资源过多，比如 I/O 与数据库的连接等

**实现：**

#### 1.1 懒汉式，线程不安全（不用）

```java
//这种方式是最基本的实现方式，这种实现最大的问题就是不支持多线程。因为没有加锁 synchronized，所以严格意义上它并不算单例模式。这种方式 lazy loading 很明显，不要求线程安全，在多线程不能正常工作。
public class Singleton {  
    private static Singleton instance;  
    private Singleton (){
        
    }  
  
    public static Singleton getInstance() {  
    	if (instance == null) {  
        	instance = new Singleton();  
    	}  
    	return instance;  
    }  
}
```

#### 1.2 懒汉式，线程安全（性能差）

```java
/*
这种方式具备很好的 lazy loading，能够在多线程中很好的工作，但是，效率很低，99% 情况下不需要同步。
优点：第一次调用才初始化，避免内存浪费。
缺点：必须加锁 synchronized 才能保证单例，但加锁会影响效率。
getInstance() 的性能对应用程序不是很关键（该方法使用不太频繁）。
*/
public class Singleton {  
    private static Singleton instance;  
    private Singleton (){
        
    }  
    public static synchronized Singleton getInstance() {  
    	if (instance == null) {  
        	instance = new Singleton();  
    	}  
    	return instance;  
    }  
}
```

#### 1.3 饿汉式（浪费资源）

```java 
/*
这种方式比较常用，但容易产生垃圾对象。
优点：没有加锁，执行效率会提高。
缺点：类加载时就初始化，浪费内存。

它基于 classloader 机制避免了多线程的同步问题，不过，instance 在类装载时就实例化，虽然导致类装载的原因有很多种，在单例模式中大多数都是调用 getInstance 方法， 但是也不能确定有其他的方式（或者其他的静态方法）导致类装载，这时候初始化 instance 显然没有达到 lazy loading 的效果。
*/
public class Singleton {  
    private static Singleton instance = new Singleton();  
    private Singleton (){
        
    }  
    public static Singleton getInstance() {  
    	return instance;  
    }  
}
```

#### 1.4 双检锁/双重校验锁（DCL，即 double-checked locking）

```java
/*
这种方式采用双锁机制，安全且在多线程情况下能保持高性能。
getInstance() 的性能对应用程序很关键。
*/
public class Singleton {  
    private volatile static Singleton singleton;  
    private Singleton (){
        
    }  
    public static Singleton getSingleton() {  
    	if (singleton == null) {  
        	synchronized (Singleton.class) {  
        		if (singleton == null) {  
           			singleton = new Singleton();  
       			}  
        	}  
    	}  
    	return singleton;  
    }  
}
```

#### 1.5 登记式/静态内部类

```java
/*
这种方式能达到双检锁方式一样的功效，但实现更简单。
对静态域使用延迟初始化，应使用这种方式而不是双检锁方式。这种方式只适用于静态域的情况，双检锁方式可在实例域需要延迟初始化时使用。
这种方式同样利用了 classloader 机制来保证初始化 instance 时只有一个线程，它跟第 3 种方式不同的是：第 3 种方式只要 Singleton 类被装载了，那么 instance 就会被实例化（没有达到 lazy loading 效果），而这种方式是 Singleton 类被装载了，instance 不一定被初始化。因为 SingletonHolder 类没有被主动使用，只有通过显式调用 getInstance 方法时，才会显式装载 SingletonHolder 类，从而实例化 instance。想象一下，如果实例化 instance 很消耗资源，所以想让它延迟加载，另外一方面，又不希望在 Singleton 类加载时就实例化，因为不能确保 Singleton 类还可能在其他的地方被主动使用从而被加载，那么这个时候实例化 instance 显然是不合适的。这个时候，这种方式相比第 3 种方式就显得很合理。
*/
public class Singleton {  
    private static class SingletonHolder {  
        //内部类不会随着外部类的调用而被加载进去
    	private static final Singleton INSTANCE = new Singleton();  
    }  
    private Singleton (){
        
    }  
    public static final Singleton getInstance() {  
    	return SingletonHolder.INSTANCE;  
    }  
}
```

#### 1.6 枚举（==拒绝反射调用==）

```java
/*
这种实现方式还没有被广泛采用，但这是实现单例模式的最佳方法。
它更简洁，自动支持序列化机制，绝对防止多次实例化。
这种方式是 Effective Java 作者 Josh Bloch 提倡的方式，它不仅能避免多线程同步问题，而且还自动支持序列化机制，防止反序列化重新创建新的对象，绝对防止多次实例化。不过，由于 JDK1.5 之后才加入 enum 特性，用这种方式写不免让人感觉生疏，在实际工作中，也很少用。
不能通过 reflection attack 来调用私有构造方法。
*/
public enum Singleton {  
    INSTANCE;  
    public void whateverMethod() { 
        
    }  
}
```

> **总结：**
>
> * 一般情况下，不建议使用第 1 种和第 2 种懒汉方式，==建议使用第 3 种饿汉方式==。
> * 只有在要明确实现 lazy loading 效果时，才会使用第 5 种登记方式。
> * 如果涉及到反序列化创建对象时，可以尝试使用第 6 种枚举方式。
> * 如果有其他特殊的需求，可以考虑使用第 4 种双检锁方式。



### 2.工厂模式



### 3.建造者模式



### 4.



------



## 六、框架



### 1. Netty

#### 1.1 Reactor

三个重要角色：

- Reactor 将I/O事件分派（dispatch）给对应的Handler
- Acceptor 处理客户端新连接，并分派请求到处理器链中（也属于特殊的Handler，专门为处理新连接，构建新Handler）
- Handlers 执行非阻塞读写任务

##### **1.1.1 单Reactor单线程模型**

<img src="D:\Program Files\Typora\java笔记资源\Reactor.png" style="zoom: 80%;" />

- Reactor单线程，负责多路分离套接字，有新的连接到来触发connect事件后，交由Acceptor进行处理，有IO读写事件之后交给hanlder 处理。
- Acceptor主要任务就是构建handler ，在获取到和client相关的SocketChannel之后 ，绑定到相应的handler上，对应的SocketChannel有读写事件之后，基于racotor 分发,hanlder就可以处理了（所有的IO事件都绑定到selector上，有Reactor分发）。

```java

  /**
    * 等待事件到来，分发事件处理
    */
  class Reactor implements Runnable {
      ServerSocketChannel serverSocket = ServerSocketChannel.open();
      Selector selector = Selector.open();
      
      private Reactor() throws Exception {
          serverSocket.configureBlocking(false);
          serverSocket.bind(new InetSocketAddress(8080));
          SelectionKey sk = serverSocket.register(selector,SelectionKey.OP_ACCEPT);
          // attach Acceptor 处理新连接
          sk.attach(new Acceptor());
      }
      
      public void run() {
          try {
              while (!Thread.interrupted()) {
                  selector.select();
                  Set<SelectionKey> selected = selector.selectedKeys();
                  Iterator<SelectionKey> it = selected.iterator();
                  while (it.hasNext()) {
                      it.remove();
                      //分发事件处理
                      dispatch(it.next());
                  }
              }
          } catch (IOException ex) {
              //do something
          }
      }
      
      void dispatch(SelectionKey k) {
          // 若是连接事件获取是Acceptor
          // 若是IO读写事件获取是IOhandler
          Runnable runnable = (Runnable) k.attachment();
          if (runnable != null) {
              runnable.run();
          }
      }
  }

  /**
    * 连接事件就绪,处理连接事件
    */
  class Acceptor implements Runnable {
      @Override
      public void run() {
          try {
              SocketChannel c = serverSocket.accept();
              if (c != null) {
                  // 注册读写
                  new IOHandler(c, selector);
              }
          } catch (Exception e) {
              
          }
      }
  }

  /**
    * 
    */
  class IOhandler implements Runnable {
      final SocketChannel channel;
      final SelectionKey k;
      IOHandler(Selector selector, SocketChannel c) throws IOException {
          channel = c;
          channel.configureBlocking(false);
          //仅仅取得选择键，稍候设置感兴趣的IO事件
          k = channel.register(selector, 0);
          //将Hanlder处理器作为选择键的附件
          k.attach(this);
          //注册读写就绪事件
          k.interestOps(SelectionKey.OP_READ|SelectionKey_WRITE);
      }
      
      public void run() {
          //处理输入输出
      }
  }
```



##### **1.1.2 单Reactor多线程模型**

<img src="D:\Program Files\Typora\java笔记资源\Reactor2.png" style="zoom:80%;" />

- Handler中使用线程池，这样业务处理线程与负责服务监听和IO事件查询的反应器线程相隔离，避免了服务器的连接监听受到阻塞。

```java
  /**
    * 多线程处理读写业务逻辑
    * 代替IOHanlder
    */
  class MultiThreadIOHandler implements Runnable {
      public static final int READING = 0, WRITING = 1;
      int state;
      final ByteBuffer buf = ByteBuffer.allocate(1024);
      final SocketChannel socket;
      final SelectionKey sk;

      //线程池处理业务逻辑
      static final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

      public MultiThreadIOHandler(SocketChannel socket, Selector sl) throws Exception {
          this.state = READING;
          this.socket = socket;
          sk = socket.register(selector, SelectionKey.OP_READ);
          sk.attach(this);
          socket.configureBlocking(false);
      }
      
      public void run() {
          pool.execute(new AsyncTask());
      }
      
      public synchronized void asyncRun() {
          try {
              if (state == WRITING) {
                  socket.write(buf);
                  buf.clear();
                  sk.interestOps(SelectionKey.OP_READ);
                  state = READING;
              } else if (state == READING) {
                  int length = 0;
                  while ((length = socket.read(buf)) > 0) {
                      System.out.prinln(new String(buf.array(), 0, length));
                  }
                  buf.flip();
                  sk.interestOps(SelectionKey.OP_WRITE);
                  state = WRITING;
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
      
      //异步任务的内部类
      class AsyncTask implements Runnable {
          public void run() {
              MultiThreadIOHanlder.this.asyncRun();
          }
      }
  }
```



##### **1.1.3 多Reactor多线程模型**

<img src="D:\Program Files\Typora\java笔记资源\Reactor3.png" style="zoom:80%;" />

- mainReactor负责监听server socket，用来处理新连接的建立，将建立的socketChannel指定注册给subReactor。
- subReactor维护自己的selector, 基于mainReactor 注册的socketChannel多路分离IO读写事件，读写网络数据，对业务处理的功能，另其扔给worker线程池来完成。

```java
  class MultiThreadIOServerReactor {
      ServerSocketChannel serverSocket;
      AtomicInteger next = new AtomicInteger(0);
      //选择器集合，引入多个选择器
      Selector[] selectors = new Selector[2];
      //引入多个子反应器
      SubReactor[] subReactor = null;
      
      MultiThreadIOServerReactor() throws IOException {
          //初始化多个选择器
          selector[0] = Selector.open();
          selector[1] = Selector.open();
          serverSocket = ServerSocketChannel.open();
          InetSocketAddress address = new InetSocketAddress(IP, PORT);
          serverSocket.socket().bind(address);
          //非阻塞
          serverSocket.configureBlocking(false);
          //第一个选择器负责监控新连接事件
          SelectionKey sk = serverSocket.register(selector[0], SelectionKey.OP_ACCEPT);
          //绑定Handler：attch 新连接监控handler处理器到SelectionKey（选择键）
          sk.attach(new AcceptHandler());
          //第一个子反应器
          SubReactor subReactor1 = new SubReactor(selector[0]);
          //第二个子反应器
          SubReactor subReactor2 = new SubReactor(selector[1]);
          subReactors = new SubReactor[]{subReactor1, subReactor2};
      }
      
      private void startService() {
          new Thread(subReactor[0]).start();
          new Thread(subReactor[1]).start();
      }
      
      //子反应器类
      class SubReactor implements Runnable {
          final Selector selector;
          public SubReactor(Selector selector) {
              this.selector = selector;
          }
          
          public void run() {
              try {
                  while (!Thread.interrupted()) {
                      selector.select();
                      Set<SelectionKey> keySet = selector.selectedKeys();
                      Iterator<SelectionKey> it = keySet.iterator();
                      while (it.hasNext()) {
                          //反应器负责dispatch（分发）收到的事件
                          SelectionKey sk = it.next();
                          dispatch(sk);
                      }
                      keySet.clear();
                  }
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          
          void dispatch(SelectionKey sk) {
              Runnable handler = (Runnable) sk.attachment();
              //调用之前attach绑定到选择器的handler处理器对象
              if (handler != null) {
                  handler.run();
              }
          }
      }
      
      //Handler：新连接处理器
      class AcceptorHandler implements Runnable {
          public void run() {
              try {
                  SocketChannel channel = serverSocket.accept();
                  if (channel != null)
                      new MultiThreadIOHandler(selector[next.get()], channel);
              } catch (IOExcception e) {
                  e.printStackTrace();
              }
              if (next.incrementAndGet() == selector.length) {
                  next.set(0);
              }
          }
      }
  }
```





### 2. SpringBoot



## 七、操作系统

### 1. IO读写的基础原理 

