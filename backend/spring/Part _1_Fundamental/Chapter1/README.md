Let's dive into the Spring Framework's **IoC Container** – the heart of Spring! 🌱 Think of it as a magical box that creates and manages your application's objects (beans) and wires them together. Instead of your objects creating their dependencies, the container **injects** them. This is **Inversion of Control (IoC)** – your code is no longer in control of instantiation; the container is! 💉

---

## 1. Introduction to the Spring IoC Container and Beans 🌱

*   **IoC (Inversion of Control):** Objects don't create their dependencies; they receive them from an external entity (the container).
*   **DI (Dependency Injection):** The main way IoC is achieved. Dependencies are "injected" via constructor arguments, setters, or factory methods.
*   **`BeanFactory` vs `ApplicationContext`**:
    *   `BeanFactory`: The most basic container, providing configuration and basic DI. 🧱
    *   `ApplicationContext`: A more advanced container built on top of `BeanFactory`. It adds enterprise features like internationalization, event publication, AOP integration, and web-specific contexts. 🏢

**Example:** Your `MovieRecommender` needs a `MovieFinder`. Instead of `new MovieFinder()`, you let Spring provide it.

---

## 2. Container Overview 🏗️

The `ApplicationContext` is your main interface to the Spring container. You feed it **configuration metadata** (instructions on what beans to create and how they relate). This metadata can be in three forms:

*   **XML configuration** (old-school, but still used in legacy projects) 📄
*   **Annotation-based configuration** (using annotations like `@Component` on classes) 🏷️
*   **Java-based configuration** (using `@Configuration` and `@Bean` methods) ☕

**Example:** Creating a container with XML:
```java
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");
```
The container reads the XML, creates beans, wires dependencies, and you can then fetch a bean with `context.getBean("beanName", BeanClass.class)`.

---

## 3. Bean Overview 🫘

A **bean** is simply an object that is instantiated, assembled, and managed by the Spring IoC container. Each bean is defined by a `BeanDefinition` containing metadata like:

*   Class name
*   Scope (singleton, prototype, etc.)
*   Constructor arguments and property values
*   Lifecycle callbacks (init, destroy)
*   References to other beans

**Naming beans:** Each bean has one or more identifiers (IDs or aliases). If not specified, Spring generates a unique name (e.g., `myMovieLister` for class `MyMovieLister`).

**Aliasing:** You can give a bean multiple names using `<alias/>` in XML or `@Bean(name = {"alias1", "alias2"})` in Java config.

**Instantiating beans:**
*   **Constructor:** `<bean class="com.example.MyClass"/>` – Spring calls `new MyClass()`.
*   **Static factory method:** Use `factory-method` to call a static method that returns the object.
*   **Instance factory method:** Use `factory-bean` and `factory-method` to call a method on another existing bean.

---

## 4. Dependencies 🔗

This is the core of DI. Beans declare what they need, and the container provides it.

### **Dependency Injection (DI) Types**

*   **Constructor-based DI:** Dependencies are provided through constructor arguments. Best for **mandatory** dependencies. 👍
    ```java
    public class MovieRecommender {
        private final MovieFinder movieFinder;
        public MovieRecommender(MovieFinder movieFinder) {
            this.movieFinder = movieFinder;
        }
    }
    ```
    In XML: `<constructor-arg ref="movieFinder"/>`

*   **Setter-based DI:** Dependencies are set via setter methods after instantiation. Best for **optional** dependencies. ✋
    ```java
    public class MovieRecommender {
        private MovieFinder movieFinder;
        public void setMovieFinder(MovieFinder movieFinder) {
            this.movieFinder = movieFinder;
        }
    }
    ```
    In XML: `<property name="movieFinder" ref="movieFinder"/>`

**Resolution process:** The container resolves dependencies when creating the bean. Circular dependencies (A depends on B, B depends on A) are mostly resolved with setter injection; constructor injection can cause `BeanCurrentlyInCreationException`.

### **Dependency Configuration Details**

You can inject:
*   **Simple values:** `<property name="url" value="jdbc:mysql://localhost:3306/mydb"/>`
*   **References to other beans:** `<property name="dataSource" ref="myDataSource"/>`
*   **Inner beans:** Define a bean inline within a property.
*   **Collections:** `<list>`, `<set>`, `<map>`, `<props>`.
*   **Null:** `<null/>`
*   **Shortcuts:** Use `p-namespace` for properties and `c-namespace` for constructors in XML.

### **Using `depends-on`**

If bean A needs bean B to be initialized **before** A (even if not a direct property), use `depends-on="beanB"`. It also ensures B is destroyed after A.

### **Lazy-initialized Beans**

By default, singleton beans are created eagerly at startup. To defer creation until first use, mark a bean as `@Lazy` or `lazy-init="true"`. But if a non-lazy bean depends on it, it gets created anyway.

### **Autowiring Collaborators**

Spring can automatically wire dependencies by **type** or **name**. Modes:
*   `no`: default, manual wiring.
*   `byName`: property name matches bean name.
*   `byType`: property type matches a single bean of that type.
*   `constructor`: like byType but for constructor args.

Useful for rapid development but can be ambiguous. You can exclude beans from autowiring with `autowire-candidate="false"`.

### **Method Injection**

When a singleton bean needs a **new instance** of a prototype bean each time a method is called, you can't just inject the prototype (it's created once). Solutions:

*   **Lookup method injection:** Use `<lookup-method>` or `@Lookup` to tell Spring to override a method and return a new prototype instance each time.
    ```java
    public abstract class CommandManager {
        protected abstract Command createCommand(); // to be overridden by Spring
    }
    ```
*   **Arbitrary method replacement:** Replace any method with a custom implementation (less common).

---

## 5. Bean Scopes 🌐

Scopes define the lifecycle of a bean instance.

| Scope       | Description                                                                                     |
|-------------|-------------------------------------------------------------------------------------------------|
| **singleton** | (Default) One instance per Spring IoC container. All requests return the same object. 🥇         |
| **prototype**  | A new instance is created every time the bean is requested. 🆕                                   |
| **request**    | One instance per HTTP request. (Web-aware context) 📨                                            |
| **session**    | One instance per HTTP session. (Web-aware) 👤                                                    |
| **application**| One instance per `ServletContext`. (Web-aware) 🌍                                                |
| **websocket**  | One instance per WebSocket session. (Web-aware) 🧦                                              |

**Singleton with prototype dependencies:** If a singleton depends on a prototype, the prototype is injected only once. To get a new prototype each time, use **method injection** or **scoped proxies**.

**Scoped beans as dependencies:** To inject a shorter-lived scope (e.g., request) into a longer-lived scope (e.g., singleton), use an AOP proxy. Define `<aop:scoped-proxy/>` or `@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)`. The proxy delegates calls to the actual scoped instance.

**Custom scopes:** You can implement your own scope (e.g., thread scope) and register it via `CustomScopeConfigurer`.

---

## 6. Customizing the Nature of a Bean ✨

Spring provides hooks to customize bean behavior.

### **Lifecycle Callbacks**

*   **Initialization:** `@PostConstruct` (recommended), `InitializingBean.afterPropertiesSet()`, or custom `init-method`.
*   **Destruction:** `@PreDestroy` (recommended), `DisposableBean.destroy()`, or custom `destroy-method`.
*   **Default init/destroy methods:** Set `default-init-method="init"` on `<beans>` to have a method named `init` called automatically on all beans.
*   **Order of execution:** `@PostConstruct` → `afterPropertiesSet` → custom init-method.

**Startup and shutdown callbacks:** Implement `Lifecycle` or `SmartLifecycle` to have your bean start/stop with the container (e.g., for background processes). `SmartLifecycle` allows phased startup/shutdown.

**Shutdown hook:** Register a shutdown hook (`context.registerShutdownHook()`) to ensure graceful destruction of singleton beans in non-web apps.

### **Aware Interfaces**

Beans can ask for container infrastructure by implementing interfaces like:
*   `ApplicationContextAware` – get the `ApplicationContext`.
*   `BeanNameAware` – get the bean's name.
*   `BeanFactoryAware` – get the `BeanFactory`.
*   `ResourceLoaderAware`, `MessageSourceAware`, etc.

*Use sparingly – they tie your code to Spring!*

---

## 7. Bean Definition Inheritance 👨‍👦

A child bean definition can inherit configuration from a parent. Useful for templating.

```xml
<bean id="baseDao" abstract="true" class="com.example.BaseDao">
    <property name="dataSource" ref="dataSource"/>
</bean>

<bean id="userDao" parent="baseDao" class="com.example.UserDao">
    <property name="sessionFactory" ref="sessionFactory"/>
</bean>
```
The `abstract` parent won't be instantiated. Child inherits `dataSource` and adds its own properties.

---

## 8. Container Extension Points 🔌

You can plug in custom logic at various points.

### **BeanPostProcessor**

Intercepts bean **instance** creation. You can modify or wrap beans after they are instantiated (but before/after initialization callbacks). Common uses: proxying, injecting custom logic.

```java
public class MyBeanPostProcessor implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        // ... before init
        return bean;
    }
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        // ... after init, maybe wrap with proxy
        return bean;
    }
}
```

### **BeanFactoryPostProcessor**

Operates on **bean definitions** (configuration metadata) before any beans are created. Used to modify property values, e.g., placeholder substitution.

**`PropertySourcesPlaceholderConfigurer`** (a `BeanFactoryPostProcessor`) resolves `${...}` placeholders in bean definitions from properties files or environment variables.

```xml
<context:property-placeholder location="classpath:app.properties"/>
```

**`PropertyOverrideConfigurer`** overrides bean property values with values from a properties file.

### **FactoryBean**

If you have complex object creation logic, implement `FactoryBean`. The container uses it to produce the actual bean. To get the factory itself, prefix bean name with `&`.

---

## 9. Annotation-based Container Configuration 🏷️

Enable annotation-driven injection with `<context:annotation-config/>` or `@ComponentScan`. This registers post-processors like `AutowiredAnnotationBeanPostProcessor`.

### **@Autowired**

Inject dependencies automatically by type.

*   On constructors (if single constructor, `@Autowired` is optional).
*   On fields, setter methods, arbitrary methods.
*   For arrays/collections: get all beans of a type.
*   For `Map<String, BeanType>`: keys are bean names.
*   Mark optional with `required=false`, `Optional`, or `@Nullable`.
*   Self-injection (reference to itself) is possible but rarely needed.

```java
@Autowired
private MovieCatalog movieCatalog;

@Autowired
public void prepare(@Qualifier("main") MovieCatalog mainCatalog, CustomerPreferenceDao dao) { ... }
```

### **@Primary / @Fallback**

When multiple beans of same type exist, mark one as `@Primary` to be the default. `@Fallback` (6.2+) marks beans that are used only if no other candidate matches.

### **@Qualifier**

Refine autowiring with qualifier values. Create custom qualifier annotations by meta-annotating with `@Qualifier`.

```java
@Autowired @Qualifier("action") private MovieCatalog actionCatalog;
```

### **Generics as Qualifiers**

If you have `Store<String>` and `Store<Integer>`, autowiring `Store<String>` will pick the one with `String` generic.

### **@Resource (JSR-250)**

Injection by name. If no name specified, uses field/setter name.

```java
@Resource(name="myMovieFinder")
private MovieFinder movieFinder;
```

### **@Value**

Inject externalized properties (from `.properties` files, environment variables) or SpEL expressions.

```java
@Value("${catalog.name:defaultCatalog}")
private String catalogName;

@Value("#{systemProperties['user.country']}")
private String country;
```

### **@PostConstruct and @PreDestroy**

Standard lifecycle annotations (from JSR-250) for initialization and destruction.

```java
@PostConstruct
public void init() { ... }
```

---

## 10. Classpath Scanning and Managed Components 🔍

Instead of explicit bean definitions, you can let Spring scan the classpath for annotated classes.

### **Stereotype Annotations**

*   `@Component` – generic component.
*   `@Repository` – DAO/repository (exception translation).
*   `@Service` – service layer.
*   `@Controller` – web controller (Spring MVC).
*   `@Configuration` – configuration class (contains `@Bean` methods).

### **@ComponentScan**

Tell Spring which packages to scan.

```java
@Configuration
@ComponentScan(basePackages = "com.example")
public class AppConfig {}
```
Or with XML: `<context:component-scan base-package="com.example"/>`

You can include/exclude filters (annotation, assignable, regex, aspectj, custom).

### **Naming Autodetected Components**

Default: lowercased non-qualified class name (e.g., `simpleMovieLister`). Override with `@Component("myName")` or `@Named("myName")`. Or provide a custom `BeanNameGenerator`.

### **Scoping Autodetected Components**

Use `@Scope` on the component class.

```java
@Scope("prototype")
@Repository
public class MovieFinderImpl { ... }
```

For web scopes, use `@RequestScope`, `@SessionScope`, etc.

### **Providing Qualifier Metadata**

Add `@Qualifier` or custom qualifier annotations on the component class.

### **Defining @Bean Methods in @Component Classes**

You can place `@Bean` methods inside a `@Component` class as well, but they are **not** enhanced with CGLIB (so calls to other `@Bean` methods inside the same class are just plain Java calls, not intercepted). Use `@Configuration` classes for inter-bean dependencies.

```java
@Component
public class FactoryMethods {
    @Bean
    public MyBean myBean() {
        return new MyBean();
    }
}
```

---

## 11. Using JSR-330 Standard Annotations 🔄

You can use `jakarta.inject` annotations instead of Spring's own, for portability.

*   `@Inject` – like `@Autowired`.
*   `@Named` – like `@Component` and also for qualifier strings.
*   `@Provider` – like Spring's `ObjectFactory`, for on-demand bean retrieval.

**Limitations:**
*   `@Inject` has no `required` attribute; use `Optional` instead.
*   No `@Value` equivalent.
*   Scopes: `@Singleton` exists, but other scopes need Spring's `@Scope`.
*   `@Named` cannot be used as a meta-annotation for custom stereotypes.

**Example:**
```java
@Named
public class SimpleMovieLister {
    @Inject
    public void setMovieFinder(@Named("main") MovieFinder movieFinder) { ... }
}
```