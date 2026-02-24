# 12. Java-based Container Configuration ☕

This is the modern way to configure Spring – using pure Java instead of XML. It's type-safe, refactorable, and much more intuitive!

### **Basic Concepts: @Bean and @Configuration** 🏗️

*   **`@Configuration`**: A class-level annotation that tells Spring "this class contains bean definitions." Think of it as a replacement for the `<beans>` XML tag. 📋
*   **`@Bean`**: A method-level annotation that tells Spring "the object returned by this method should be registered as a bean." Think of it as a replacement for the `<bean>` XML tag. 🏷️

**Simple Example:**
```java
@Configuration
public class AppConfig {
    
    @Bean
    public MyService myService() {
        return new MyServiceImpl(); // Creates and returns a bean
    }
}
```
This is equivalent to XML: `<bean id="myService" class="com.example.MyServiceImpl"/>`

**Important Note on @Bean Methods:**
- In `@Configuration` classes, Spring **proxies** your class (using CGLIB) to ensure that calls to `@Bean` methods return the same singleton instance, not a new one each time. 🔄
- If you use `@Bean` in a regular `@Component` class (or `@Configuration(proxyBeanMethods = false)`), it's "lite mode" – no proxying, so each call creates a new instance! ⚠️

---

### **Instantiating the Container with AnnotationConfigApplicationContext** 🚀

This is your go-to class for Java-based configuration.

**Simple construction:**
```java
public static void main(String[] args) {
    ApplicationContext context = 
        new AnnotationConfigApplicationContext(AppConfig.class);
    
    MyService service = context.getBean(MyService.class);
    service.doStuff();
}
```

**Programmatic registration:**
```java
AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
ctx.register(AppConfig.class, OtherConfig.class);
ctx.refresh(); // Don't forget to refresh!
```

**With component scanning:**
```java
AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
ctx.scan("com.example"); // Scans package for @Component classes
ctx.refresh();
```

**For web applications:** Use `AnnotationConfigWebApplicationContext` in your `web.xml` instead of the XML-based version.

---

### **Using the @Bean Annotation** 🫘

**Declaring a bean:** Method name = bean name (by default).
```java
@Bean
public TransferService transferService() {
    return new TransferServiceImpl();
}
```

**Bean dependencies:** Just add parameters!
```java
@Bean
public TransferService transferService(AccountRepository accountRepository) {
    return new TransferServiceImpl(accountRepository); // Dependency injected!
}
```

**Lifecycle callbacks:**
```java
@Bean(initMethod = "init", destroyMethod = "cleanup")
public BeanOne beanOne() {
    return new BeanOne();
}
```
**Pro tip:** Spring automatically detects `close()` or `shutdown()` methods! If you don't want that, use `@Bean(destroyMethod = "")` to disable. 🛑

**Specifying scope:**
```java
@Bean
@Scope("prototype")
public Encryptor encryptor() {
    return new Encryptor();
}
```

**Custom bean name and aliases:**
```java
@Bean({"dataSource", "subsystemA-dataSource"}) // Multiple names!
public DataSource dataSource() {
    return new DriverManagerDataSource();
}
```

**Adding a description:**
```java
@Bean
@Description("Provides a basic example of a bean")
public Thing thing() {
    return new Thing();
}
```

---

### **Using the @Configuration Annotation** 🏛️

**Inter-bean dependencies:** One bean method calling another.
```java
@Configuration
public class AppConfig {
    
    @Bean
    public BeanOne beanOne() {
        return new BeanOne(beanTwo()); // Calls another @Bean method
    }
    
    @Bean
    public BeanTwo beanTwo() {
        return new BeanTwo();
    }
}
```
**Magic:** Even though `beanTwo()` is called, Spring ensures the same singleton instance is returned! ✨

**Lookup method injection (for prototype beans in singletons):**
```java
@Bean
public CommandManager commandManager() {
    return new CommandManager() {
        protected Command createCommand() {
            return asyncCommand(); // Returns new prototype each time
        }
    };
}

@Bean
@Scope("prototype")
public AsyncCommand asyncCommand() {
    return new AsyncCommand();
}
```

**How it works internally:** Spring creates a CGLIB subclass of your `@Configuration` class. The overridden `@Bean` methods first check the container for an existing bean before calling the parent method. Smart! 🧠

---

### **Composing Java-based Configurations** 🧩

**Using @Import:** Combine multiple configuration classes.
```java
@Configuration
public class ConfigA {
    @Bean public A a() { return new A(); }
}

@Configuration
@Import(ConfigA.class)
public class ConfigB {
    @Bean public B b() { return new B(); }
}

// Just need to register ConfigB!
ApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigB.class);
```

**Dependencies across imported configs:** Use method parameters!
```java
@Configuration
public class ServiceConfig {
    @Bean
    public TransferService transferService(AccountRepository accountRepository) {
        return new TransferServiceImpl(accountRepository);
    }
}

@Configuration
public class RepositoryConfig {
    @Bean
    public AccountRepository accountRepository(DataSource dataSource) {
        return new JdbcAccountRepository(dataSource);
    }
}

@Configuration
@Import({ServiceConfig.class, RepositoryConfig.class})
public class SystemTestConfig {
    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource();
    }
}
```
Spring wires it all together automatically! 🤝

**Alternative: Autowiring config classes**
```java
@Configuration
public class ServiceConfig {
    @Autowired
    private RepositoryConfig repositoryConfig; // Inject the whole config!
    
    @Bean
    public TransferService transferService() {
        return new TransferServiceImpl(repositoryConfig.accountRepository());
    }
}
```
This makes dependencies more explicit but creates tighter coupling.

**Conditional configuration with @Profile:**
```java
@Configuration
@Profile("development")
public class DevConfig {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().build();
    }
}

@Configuration
@Profile("production")
public class ProdConfig {
    @Bean
    public DataSource dataSource() throws Exception {
        return (DataSource) new InitialContext().lookup("java:comp/env/jdbc/datasource");
    }
}
```

**Combining Java and XML with @ImportResource:**
```java
@Configuration
@ImportResource("classpath:/com/acme/properties-config.xml")
public class AppConfig {
    @Value("${jdbc.url}")
    private String url;
    
    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource(url, username, password);
    }
}
```

---

### **Programmatic Bean Registration** 👨‍💻

For ultimate flexibility, you can register beans programmatically using `BeanRegistrar`.

```java
class MyBeanRegistrar implements BeanRegistrar {
    @Override
    public void register(BeanRegistry registry, Environment env) {
        // Register a simple bean
        registry.registerBean("foo", Foo.class);
        
        // Register with custom configuration
        registry.registerBean("bar", Bar.class, spec -> spec
            .prototype()
            .lazyInit()
            .supplier(context -> new Bar(context.bean(Foo.class))));
        
        // Conditional registration
        if (env.matchesProfiles("baz")) {
            registry.registerBean(Baz.class);
        }
    }
}

@Configuration
@Import(MyBeanRegistrar.class)
class MyConfiguration {}
```
This is perfect for advanced scenarios like dynamic bean registration based on runtime conditions! 🔧

---

## 13. Environment Abstraction 🌍

The `Environment` interface is your window into the application's runtime environment, handling two key aspects: **profiles** and **properties**.

### **Bean Definition Profiles** 🎭

Profiles let you register different beans for different environments (dev, test, prod).

**Why profiles?** Different environments need different configurations:
- Dev: Embedded database, verbose logging 📝
- Prod: JNDI datasource from app server, minimal logging 🏭

**Using @Profile:**
```java
@Configuration
@Profile("development")
public class StandaloneDataConfig {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("schema.sql")
            .build();
    }
}

@Configuration
@Profile("production")
public class JndiDataConfig {
    @Bean
    public DataSource dataSource() throws Exception {
        Context ctx = new InitialContext();
        return (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
    }
}
```

**Profile expressions:** You can use logical operators!
- `@Profile("!dev")` – NOT dev
- `@Profile("prod & us-east")` – prod AND us-east
- `@Profile("prod | staging")` – prod OR staging

**Method-level @Profile:** Choose which beans to include per profile.
```java
@Configuration
public class AppConfig {
    @Bean("dataSource")
    @Profile("development")
    public DataSource devDataSource() { ... }
    
    @Bean("dataSource")
    @Profile("production")
    public DataSource prodDataSource() { ... }
}
```

**XML profiles:** Also available in XML!
```xml
<beans profile="development">
    <jdbc:embedded-database id="dataSource"/>
</beans>

<beans profile="production">
    <jee:jndi-lookup id="dataSource" jndi-name="..."/>
</beans>
```

**Activating profiles:**
- Programmatically: `ctx.getEnvironment().setActiveProfiles("dev", "test")`
- System property: `-Dspring.profiles.active=dev,test`
- Environment variable: `SPRING_PROFILES_ACTIVE=dev,test`
- Web.xml: `<context-param>` with name `spring.profiles.active`

**Default profile:** If no profile is active, the "default" profile is used.
```java
@Configuration
@Profile("default") // Activated when no other profile is active
public class DefaultDataConfig { ... }
```

---

### **PropertySource Abstraction** 📋

The `Environment` provides a unified search over multiple property sources (system properties, environment variables, property files, etc.).

**Default property sources (StandardEnvironment):**
1. JVM system properties (`System.getProperties()`) ⚙️
2. System environment variables (`System.getenv()`) 🌐

**For web apps (StandardServletEnvironment), additional sources:**
3. ServletConfig parameters
4. ServletContext parameters
5. JNDI entries (`java:comp/env/`)

**Hierarchy matters:** Earlier sources override later ones!

**Using @PropertySource:**
```java
@Configuration
@PropertySource("classpath:/com/myco/app.properties")
public class AppConfig {
    @Autowired
    Environment env;
    
    @Bean
    public TestBean testBean() {
        TestBean bean = new TestBean();
        bean.setName(env.getProperty("testbean.name"));
        return bean;
    }
}
```

**Placeholder resolution in @PropertySource:**
```java
@PropertySource("classpath:/com/${my.placeholder:default/path}/app.properties")
```
The placeholder is resolved against already-registered property sources! 🔄

**Adding custom PropertySource programmatically:**
```java
ConfigurableApplicationContext ctx = new GenericApplicationContext();
MutablePropertySources sources = ctx.getEnvironment().getPropertySources();
sources.addFirst(new MyPropertySource()); // Highest priority!
```

---

## 14. Registering a LoadTimeWeaver 🔧

Load-time weaving allows you to transform classes as they're loaded into the JVM – useful for AspectJ and JPA entity enhancement.

**Enable it easily:**
```java
@Configuration
@EnableLoadTimeWeaving
public class AppConfig { }
```

Or in XML:
```xml
<context:load-time-weaver/>
```

Once enabled, beans can implement `LoadTimeWeaverAware` to get the weaver instance.

---

## 15. Additional Capabilities of the ApplicationContext 🎁

The `ApplicationContext` isn't just a `BeanFactory` – it's a super-powered container with enterprise features!

### **Internationalization (i18n) with MessageSource** 🌐

**What it does:** Resolves messages in different languages.

**Configuration:**
```xml
<bean id="messageSource" 
      class="org.springframework.context.support.ResourceBundleMessageSource">
    <property name="basenames">
        <list>
            <value>messages</value>
            <value>errors</value>
        </list>
    </property>
</bean>
```

**Property files:**
- `messages.properties` (default): `welcome=Welcome {0}!`
- `messages_es.properties`: `welcome=¡Bienvenido {0}!`

**Usage:**
```java
public class MyService {
    @Autowired
    private MessageSource messageSource;
    
    public void greet(String name) {
        String message = messageSource.getMessage(
            "welcome", 
            new Object[]{name}, 
            Locale.US
        );
        System.out.println(message);
    }
}
```

**MessageSourceAware:** Beans can implement this to get the `MessageSource` injected automatically.

---

### **Standard and Custom Events** 📢

Spring's event mechanism lets beans communicate in a decoupled way.

**Built-in events:**
- `ContextRefreshedEvent` – context initialized or refreshed
- `ContextStartedEvent` – `start()` called on context
- `ContextStoppedEvent` – `stop()` called on context
- `ContextClosedEvent` – context closing
- `RequestHandledEvent` – web request completed

**Creating custom events:**
```java
// 1. Create the event class
public class BlockedListEvent extends ApplicationEvent {
    private final String address;
    private final String content;
    
    public BlockedListEvent(Object source, String address, String content) {
        super(source);
        this.address = address;
        this.content = content;
    }
    // getters...
}
```

**Publishing events:**
```java
@Component
public class EmailService implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;
    
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
    
    public void sendEmail(String address, String content) {
        if (isBlocked(address)) {
            publisher.publishEvent(new BlockedListEvent(this, address, content));
            return;
        }
        // send email...
    }
}
```

**Listening to events (traditional way):**
```java
@Component
public class BlockedListNotifier implements ApplicationListener<BlockedListEvent> {
    public void onApplicationEvent(BlockedListEvent event) {
        System.out.println("Blocked: " + event.getAddress());
    }
}
```

**Modern way – @EventListener (much cleaner!):**
```java
@Component
public class BlockedListNotifier {
    @EventListener
    public void handleBlockedListEvent(BlockedListEvent event) {
        System.out.println("Blocked: " + event.getAddress());
    }
}
```

**Conditional event handling:**
```java
@EventListener(condition = "#event.content == 'spam'")
public void handleSpamEvent(BlockedListEvent event) { ... }
```

**Returning events:** Publish another event as a result!
```java
@EventListener
public ListUpdateEvent handleBlockedListEvent(BlockedListEvent event) {
    // do something
    return new ListUpdateEvent(this); // This gets published automatically!
}
```

**Async events:** Combine with `@Async` for non-blocking processing.
```java
@EventListener
@Async
public void handleBlockedListEvent(BlockedListEvent event) { ... }
```

**Ordering listeners:** Use `@Order` to control execution order.
```java
@EventListener
@Order(1)
public void firstHandler(BlockedListEvent event) { ... }

@EventListener
@Order(2)
public void secondHandler(BlockedListEvent event) { ... }
```

**Generic events:** Type-safe events with generics.
```java
public class EntityCreatedEvent<T> extends ApplicationEvent 
                                   implements ResolvableTypeProvider {
    public EntityCreatedEvent(T entity) {
        super(entity);
    }
    
    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(
            getClass(), 
            ResolvableType.forInstance(getSource())
        );
    }
}

// Listener for Person entities only!
@EventListener
public void onPersonCreated(EntityCreatedEvent<Person> event) { ... }
```

---

### **Convenient Access to Low-level Resources** 📁

The `ApplicationContext` is a `ResourceLoader` – it can load resources from anywhere!

**Loading resources:**
```java
@Autowired
private ResourceLoader resourceLoader;

public void loadResource() throws IOException {
    Resource resource = resourceLoader.getResource("classpath:data.txt");
    // or "file:/tmp/data.txt", "https://example.com/data.txt", etc.
    
    InputStream is = resource.getInputStream();
    // use the stream...
}
```

**Resource injection:**
```java
@Component
public class MyComponent {
    @Value("classpath:config.properties")
    private Resource configFile;
    
    @PostConstruct
    public void init() throws IOException {
        Properties props = new Properties();
        props.load(configFile.getInputStream());
    }
}
```

**ResourceLoaderAware:** Beans can implement this to get the `ResourceLoader` injected.

---

### **Application Startup Tracking** ⏱️

Spring can track where time is spent during application startup!

**How it works:** The `ApplicationContext` uses `ApplicationStartup` to record `StartupStep`s during:
- Package scanning
- Configuration class processing
- Bean instantiation
- Event processing

**Enabling Flight Recorder tracking:**
```java
AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
ctx.setApplicationStartup(new FlightRecorderApplicationStartup());
ctx.register(AppConfig.class);
ctx.refresh();
```

**Custom startup steps:**
```java
@Component
public class MyComponent implements ApplicationStartupAware {
    private ApplicationStartup applicationStartup;
    
    public void setApplicationStartup(ApplicationStartup startup) {
        this.applicationStartup = startup;
    }
    
    public void initialize() {
        StartupStep step = applicationStartup.start("my.custom.step");
        step.tag("key", "value");
        try {
            // do work
        } finally {
            step.end();
        }
    }
}
```

---

### **Convenient Web Application Instantiation** 🌐

For web apps, use `ContextLoaderListener` in `web.xml`:

```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/daoContext.xml /WEB-INF/applicationContext.xml</param-value>
</context-param>

<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

The listener creates the root `ApplicationContext` and makes it available via `WebApplicationContextUtils.getWebApplicationContext(servletContext)`.

---

### **Deploying as a Jakarta EE RAR File** 📦

You can deploy a Spring `ApplicationContext` as a RAR (Resource Adapter) file in a Jakarta EE server – perfect for message-driven or scheduled jobs without a web interface.

**Steps:**
1. Package classes in a RAR file (JAR with .rar extension)
2. Add dependencies to RAR root
3. Add `META-INF/ra.xml` and `META-INF/applicationContext.xml`
4. Deploy to app server

This lets your Spring beans use server resources like JTA transactions and JNDI data sources!

---

## 16. The BeanFactory API 🔧

The `BeanFactory` is the foundation of Spring's IoC – it's what does the actual bean creation and wiring.

**Key implementations:**
- `DefaultListableBeanFactory` – the workhorse implementation

**When to use BeanFactory vs ApplicationContext:**

| Feature | BeanFactory | ApplicationContext |
|---------|-------------|-------------------|
| Bean instantiation/wiring | ✅ | ✅ |
| Integrated lifecycle management | ❌ | ✅ |
| Auto BeanPostProcessor registration | ❌ | ✅ |
| Auto BeanFactoryPostProcessor registration | ❌ | ✅ |
| MessageSource (i18n) | ❌ | ✅ |
| Event publication | ❌ | ✅ |

**Rule of thumb:** Always use `ApplicationContext` unless you have a very specific reason not to! It's just `BeanFactory` with superpowers. 💪

**Programmatic BeanFactory usage (rare):**
```java
DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

// Load bean definitions
XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
reader.loadBeanDefinitions(new FileSystemResource("beans.xml"));

// Manually register post-processors (normally auto-detected!)
factory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());

// Apply property placeholders manually
PropertySourcesPlaceholderConfigurer cfg = new PropertySourcesPlaceholderConfigurer();
cfg.setLocation(new FileSystemResource("jdbc.properties"));
cfg.postProcessBeanFactory(factory);

// Now use the factory
MyBean bean = factory.getBean(MyBean.class);
```

As you can see, this is much more cumbersome – another reason to stick with `ApplicationContext`! 😊

---

## Congratulations! 🎉

You've now mastered the Spring IoC container – from basic concepts to advanced configuration! You understand:

- ✅ What IoC and DI are and why they matter
- ✅ How to configure beans with XML, annotations, and Java config
- ✅ How dependencies are resolved and injected
- ✅ Bean scopes and lifecycle callbacks
- ✅ Advanced topics like profiles, properties, events, and AOP integration

This foundation will serve you well as you explore other Spring modules like Spring MVC, Spring Data, Spring Boot, and more! The IoC container is the heart of Spring – and now you know exactly how it beats! ❤️

Happy coding! 🚀