# 🌱 Spring IoC Container: A Comprehensive Guide

This guide covers Spring’s **Inversion of Control (IoC)** container – the core of the Spring Framework. You’ll learn how to configure and manage beans, wire dependencies, and leverage advanced features like profiles, events, and internationalization. Each section includes practical examples and key takeaways.


## 📚 Table of Contents

1. [Introduction to the Spring IoC Container and Beans](#introduction)
2. [Container Overview](#container-overview)
3. [Bean Overview](#bean-overview)
4. [Dependencies](#dependencies)
5. [Bean Scopes](#bean-scopes)
6. [Customizing the Nature of a Bean](#customizing-beans)
7. [Bean Definition Inheritance](#bean-inheritance)
8. [Container Extension Points](#extension-points)
9. [Annotation-based Container Configuration](#annotation-config)
10. [Classpath Scanning and Managed Components](#classpath-scanning)
11. [Using JSR-330 Standard Annotations](#jsr330)
12. [Java-based Container Configuration](#java-config)
13. [Environment Abstraction](#environment)
14. [Registering a LoadTimeWeaver](#loadtimeweaver)
15. [Additional Capabilities of the ApplicationContext](#appcontext-capabilities)
16. [The BeanFactory API](#beanfactory-api)

---

## 1. Introduction to the Spring IoC Container and Beans <a name="introduction"></a>

**Inversion of Control (IoC)** means the container controls the lifecycle and dependencies of objects, rather than objects controlling them themselves. **Dependency Injection (DI)** is the primary way IoC is achieved: objects define their dependencies (through constructors, setters, or factory methods), and the container injects them.

- **`BeanFactory`**: The most basic container, providing configuration and DI.
- **`ApplicationContext`**: A more advanced container that adds enterprise features like internationalization, event publication, and AOP integration.

**Example:**
```java
// Define a bean class
public class MovieRecommender {
    private final MovieFinder movieFinder;

    public MovieRecommender(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }
}
```

---

## 2. Container Overview <a name="container-overview"></a>

The `ApplicationContext` is the main interface to the Spring container. It reads **configuration metadata** (XML, annotations, or Java config) to create and wire beans.

**Common implementations:**
- `ClassPathXmlApplicationContext` – loads XML from classpath.
- `AnnotationConfigApplicationContext` – loads Java-based configuration.

**Example:**
```java
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml");
PetStoreService service = context.getBean("petStore", PetStoreService.class);
```

---

## 3. Bean Overview <a name="bean-overview"></a>

A **bean** is an object managed by the Spring container. Bean definitions (represented by `BeanDefinition`) contain metadata:

- Class name
- Scope (singleton, prototype, etc.)
- Constructor arguments and property values
- Lifecycle callbacks
- References to other beans

**Naming:** Each bean has one or more identifiers (IDs/aliases). If not specified, Spring generates a name (e.g., `myMovieLister` for class `MyMovieLister`).

**Instantiating beans:**
- **Constructor:** `<bean class="com.example.MyClass"/>`
- **Static factory method:** Use `factory-method`
- **Instance factory method:** Use `factory-bean` and `factory-method`

---

## 4. Dependencies <a name="dependencies"></a>

### Dependency Injection (DI) Types

- **Constructor-based**: Dependencies provided via constructor arguments – best for **mandatory** dependencies.
  ```java
  public class MovieRecommender {
      public MovieRecommender(MovieFinder movieFinder) { ... }
  }
  ```
- **Setter-based**: Dependencies set via setter methods – best for **optional** dependencies.
  ```java
  public void setMovieFinder(MovieFinder movieFinder) { ... }
  ```

### Dependency Configuration

- **Simple values:** `<property name="url" value="jdbc:mysql://localhost/mydb"/>`
- **References:** `<property name="dataSource" ref="myDataSource"/>`
- **Collections:** `<list/>`, `<set/>`, `<map/>`, `<props/>`
- **Null:** `<null/>`

### Using `depends-on`

Force bean B to be initialized before bean A (even if not a direct reference).
```xml
<bean id="beanOne" class="ExampleBean" depends-on="manager"/>
```

### Lazy-initialized Beans

By default, singleton beans are created at startup. To defer creation until first use, mark as `@Lazy` or `lazy-init="true"`.

### Autowiring

Spring can automatically wire dependencies by **type** or **name**. Modes: `no`, `byName`, `byType`, `constructor`. Use with caution – can be ambiguous.

### Method Injection

When a singleton needs a **new instance** of a prototype bean each time, use lookup method injection:
```java
public abstract class CommandManager {
    protected abstract Command createCommand(); // overridden by Spring
}
```
```xml
<lookup-method name="createCommand" bean="myCommand"/>
```

---

## 5. Bean Scopes <a name="bean-scopes"></a>

| Scope       | Description                                                                 |
|-------------|-----------------------------------------------------------------------------|
| **singleton** | (Default) One instance per container.                                       |
| **prototype**  | New instance every time the bean is requested.                              |
| **request**    | One instance per HTTP request (web-aware).                                  |
| **session**    | One instance per HTTP session (web-aware).                                  |
| **application**| One instance per `ServletContext` (web-aware).                              |
| **websocket**  | One instance per WebSocket session (web-aware).                             |

**Singleton with prototype dependency:** To get a new prototype each time, use **method injection** or **scoped proxies** (`<aop:scoped-proxy/>` or `@Scope(proxyMode = ...)`).

**Custom scopes:** Implement `Scope` and register via `CustomScopeConfigurer`.

---

## 6. Customizing the Nature of a Bean <a name="customizing-beans"></a>

### Lifecycle Callbacks

- **Initialization:** `@PostConstruct` (recommended), `InitializingBean.afterPropertiesSet()`, or custom `init-method`.
- **Destruction:** `@PreDestroy` (recommended), `DisposableBean.destroy()`, or custom `destroy-method`.
- **Order:** `@PostConstruct` → `afterPropertiesSet` → custom init.

**Default init/destroy methods:** Set `default-init-method` and `default-destroy-method` on `<beans>`.

**Startup/Shutdown:** Implement `Lifecycle` or `SmartLifecycle` for background processes.

### Aware Interfaces

Beans can ask for container infrastructure:
- `ApplicationContextAware` – get the `ApplicationContext`.
- `BeanNameAware` – get the bean's name.
- `BeanFactoryAware` – get the `BeanFactory`.
- And more (`ResourceLoaderAware`, `MessageSourceAware`, etc.)

*Use sparingly – they tie your code to Spring!*

---

## 7. Bean Definition Inheritance <a name="bean-inheritance"></a>

A child bean definition can inherit configuration from a parent. Useful for templating.

```xml
<bean id="baseDao" abstract="true">
    <property name="dataSource" ref="dataSource"/>
</bean>

<bean id="userDao" parent="baseDao" class="com.example.UserDao">
    <property name="sessionFactory" ref="sessionFactory"/>
</bean>
```

The `abstract` parent is never instantiated.

---

## 8. Container Extension Points <a name="extension-points"></a>

### BeanPostProcessor

Intercepts bean **instance** creation – can modify or wrap beans after instantiation.
```java
public class MyBeanPostProcessor implements BeanPostProcessor {
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        // wrap with proxy, etc.
        return bean;
    }
}
```

### BeanFactoryPostProcessor

Operates on **bean definitions** before any beans are created. Used for property placeholders, etc.

**`PropertySourcesPlaceholderConfigurer`** resolves `${...}` placeholders from properties files or environment.
```xml
<context:property-placeholder location="classpath:app.properties"/>
```

**`PropertyOverrideConfigurer`** overrides bean property values from a properties file.

### FactoryBean

For complex object creation logic. Implement `FactoryBean`; container uses it to produce the actual bean. Prefix bean name with `&` to get the factory itself.

---

## 9. Annotation-based Container Configuration <a name="annotation-config"></a>

Enable with `<context:annotation-config/>` or `@ComponentScan`.

### @Autowired

Inject by type.
- On constructors (optional if single constructor)
- On fields, setter methods, arbitrary methods
- Arrays/collections get all beans of that type
- Mark optional with `required=false`, `Optional`, or `@Nullable`

### @Primary / @Fallback

When multiple beans of same type exist, mark one as `@Primary` to be the default. `@Fallback` (6.2+) is used if no other candidate matches.

### @Qualifier

Refine autowiring with qualifier values. Create custom qualifier annotations by meta-annotating with `@Qualifier`.

### Generics as Qualifiers

Autowiring `Store<String>` will pick the bean with `String` generic.

### @Resource (JSR-250)

Injection by name.
```java
@Resource(name="myMovieFinder")
private MovieFinder movieFinder;
```

### @Value

Inject externalized properties or SpEL expressions.
```java
@Value("${catalog.name:defaultCatalog}")
private String catalogName;
```

### @PostConstruct and @PreDestroy

Standard lifecycle annotations.

---

## 10. Classpath Scanning and Managed Components <a name="classpath-scanning"></a>

Stereotype annotations:
- `@Component` – generic component.
- `@Repository` – DAO (exception translation).
- `@Service` – service layer.
- `@Controller` – web controller.
- `@Configuration` – configuration class.

**Enable scanning:**
```java
@Configuration
@ComponentScan(basePackages = "com.example")
public class AppConfig {}
```
Or XML: `<context:component-scan base-package="com.example"/>`

**Naming:** Default bean name = lowercased non-qualified class name. Override with `@Component("myName")` or `@Named`.

**Scoping:** Use `@Scope` on the component class.

**Providing qualifiers:** Add `@Qualifier` or custom qualifier annotations on the component class.

**@Bean methods in @Component classes:** Allowed, but not enhanced (no CGLIB) – use `@Configuration` for inter-bean dependencies.

---

## 11. Using JSR-330 Standard Annotations <a name="jsr330"></a>

Use `jakarta.inject` annotations for portability.
- `@Inject` – like `@Autowired`.
- `@Named` – like `@Component` and also for qualifier strings.
- `@Provider` – like `ObjectFactory` for on-demand retrieval.

**Limitations:**
- No `@Value` equivalent.
- `@Inject` has no `required` attribute; use `Optional`.
- `@Named` cannot be used as meta-annotation for custom stereotypes.

---

## 12. Java-based Container Configuration <a name="java-config"></a>

### @Bean and @Configuration

- `@Configuration` – class containing bean definitions.
- `@Bean` – method returning a bean instance.

**Example:**
```java
@Configuration
public class AppConfig {
    @Bean
    public MyService myService() {
        return new MyServiceImpl();
    }
}
```

**Inter-bean dependencies:** One `@Bean` method can call another – Spring ensures singleton reuse (due to CGLIB proxying in `@Configuration` classes).

**Lifecycle:** `initMethod` and `destroyMethod` attributes, or `@PostConstruct`/`@PreDestroy`.

**Scope:** `@Scope("prototype")` etc.

**Conditional configuration:** `@Profile` or `@Conditional`.

**Importing other configs:** `@Import` and `@ImportResource`.

**Programmatic registration:** Use `BeanRegistrar` for dynamic bean registration.

---

## 13. Environment Abstraction <a name="environment"></a>

The `Environment` object handles **profiles** and **properties**.

### Profiles

Group beans for different environments.
```java
@Configuration
@Profile("development")
public class DevConfig { ... }
```

**Activation:** `-Dspring.profiles.active=dev,test` or programmatically `ctx.getEnvironment().setActiveProfiles("dev")`.

**Default profile:** "default" – used when no profile is active.

### PropertySource Abstraction

Hierarchical search over property sources (system properties, env vars, property files, JNDI, etc.).

**@PropertySource:**
```java
@Configuration
@PropertySource("classpath:/app.properties")
public class AppConfig {
    @Autowired Environment env;
}
```

**Placeholder resolution:** `${...}` in XML or `@Value` resolved via `Environment`.

**Custom PropertySource:** Add programmatically via `MutablePropertySources`.

---

## 14. Registering a LoadTimeWeaver <a name="loadtimeweaver"></a>

Enables class transformation at load time (AspectJ, JPA).
```java
@Configuration
@EnableLoadTimeWeaving
public class AppConfig {}
```
Or XML: `<context:load-time-weaver/>`

Beans can implement `LoadTimeWeaverAware` to get the weaver.

---

## 15. Additional Capabilities of the ApplicationContext <a name="appcontext-capabilities"></a>

### Internationalization (MessageSource)

Resolve messages for different locales.
```java
@Autowired
private MessageSource messageSource;

String msg = messageSource.getMessage("welcome", new Object[]{"John"}, Locale.US);
```

### Events

Publish and listen to events.
- **Built-in events:** `ContextRefreshedEvent`, `ContextStartedEvent`, etc.
- **Custom events:** Extend `ApplicationEvent` and use `ApplicationEventPublisher`.
- **@EventListener:** Simplified listener with condition support.
- **Async events:** Combine with `@Async`.

### Resource Loading

`ApplicationContext` is a `ResourceLoader`.
```java
Resource resource = ctx.getResource("classpath:data.txt");
```

### Application Startup Tracking

Record startup steps with `ApplicationStartup` (e.g., for Java Flight Recorder).

### Web Application Support

`ContextLoaderListener` bootstraps root context in web apps.

### RAR Deployment

Deploy Spring context as a Jakarta EE RAR file (for non-web modules).

---

## 16. The BeanFactory API <a name="beanfactory-api"></a>

`BeanFactory` is the foundation. `ApplicationContext` adds enterprise features.

| Feature | BeanFactory | ApplicationContext |
|---------|-------------|-------------------|
| Bean instantiation/wiring | ✅ | ✅ |
| Lifecycle management | ❌ | ✅ |
| Auto post-processor registration | ❌ | ✅ |
| MessageSource (i18n) | ❌ | ✅ |
| Event publication | ❌ | ✅ |

**When to use `BeanFactory` directly?** Rare – only if you need extreme lightweightness or custom bootstrapping. Otherwise, always prefer `ApplicationContext`.
