### tomcat 三件微小的工作：
servlet 容器，针对一个 servlet 的 request 请求，就做了三件很微小的事:
* 创建一个实现了 javax.servlet.ServletRequest 接口或 javax.servlet.http.ServletRequest 接口的 HttpServletRequest 类，该类实例化一个 Request 对象，并用请求参数、请求头(headers)、cookies、查询字符串、uri 等信息填充该 Request 对象
    *  个人感觉这些东西（比如接口的名字等）没有必要记，主要是学习掌握 tomcat 这个优秀开源容器的设计思路，并能用到实际项目中。
* 创建一个实现了 javax.servlet.ServletResponse 接口或 javax.servlet.http.ServletResponse 接口的 HttpServletResponse 类，该类实例化一个 Response 对象
* 调用相应的 servlet 的服务方法，将先前创建的 request 对象和 response 对象作为参数传入。接收请求的 servlet 从 request 对象中读取信息，并将返回值写入到 response 对象
    *  其实很简单的一个模型，eg；</br>
` doSth (HttpServletRequest request, HttpServletResponse response) `

<b>就是三件事，很微小的工作：一个请求处理类，一个响应处理类，一个服务类。</b>

#### catalina
catalina 是 servlet 容器的代称，它的设计开发结构十分优雅，功能结构十分的模块化。

从 servlet 容器的功能角度看，catalian 可以划分为两大模块: connector 模块和 container 模块。
</br>
![](src/main/resources/image/study/catalina01.png)

1. connector 将用户请求与 container 连接
    1.  connector 是为每个它收到的 HTTP 请求建立 request 对象和 response 对象。
    2.  将处理过程交给 container 模块。
2. container 模块从 connector 模块中收到 request 对象和 response 对象，并做一些预处理工作，比如身份认证、session创建等，最后才调用相应的 servlet 服务方法

##### 综上，可以知道，深入 tomcat，首先是从 connector 入手。