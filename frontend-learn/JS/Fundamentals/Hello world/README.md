# 🌍 Hello, World! – Understanding Script Markup & External Files

In HTML, JavaScript is added using the `<script>` tag. While older versions of HTML used extra attributes, modern web development keeps things clean and simple.

## ✅ Modern Script Tag

A basic script tag looks like this:

```html
<script>
  alert("Hello, World!");
</script>
```

You don't need to add any extra attributes — browsers recognize it as JavaScript by default.



## 🚫 Obsolete Attributes (Don’t Use Them)

### `type="..."` Attribute (Old usage)

```html
<!-- Old HTML4 style -->
<script type="text/javascript">
  alert("Hello");
</script>
```

- ❌ Not required anymore.
- ✅ Now used for **JavaScript modules** (`type="module"`), which we'll cover later.

### `language="..."` Attribute

```html
<!-- Very old and outdated -->
<script language="javascript">
  alert("Hello");
</script>
```

- 🛑 Used in ancient HTML versions.
- 🧼 No longer needed or supported today.

Stick with the simple `<script>` tag — modern browsers understand JavaScript by default.



## 📁 External Scripts

If you have a lot of JavaScript code, it’s better to put it into a **separate file**. This keeps your HTML clean and improves performance.

Use the `src` attribute to link an external script:

```html
<script src="/path/to/script.js"></script>
```

- `/path/to/script.js` is an **absolute path** from the site root.
- You can also use a **relative path** like `src="script.js"` or `src="./script.js"`.

### 🔗 Examples:

- From CDN (Content Delivery Network):

```html
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.11/lodash.js"></script>
```

- From your own server:

```html
<script src="/js/script1.js"></script>
<script src="/js/script2.js"></script>
```

You can include multiple scripts on one page by using multiple `<script>` tags.


## ⚠️ Important Notes

- **Only the simplest scripts** should be placed directly in HTML. More complex logic belongs in external files.
- External scripts are **cached** by the browser. That means:
  - First time a user visits your site, the browser downloads the script.
  - On future visits, it loads from cache — making your pages faster!

> 📦 Tip: Caching helps reduce traffic and improves load times across your website.



## ⛔ Don’t Mix `src` and Inline Code

If the `src` attribute is set, the content inside the `<script>` tag is **ignored**.

This won’t work:

```html
<script src="file.js">
  alert(1); <!-- This code will NOT run -->
</script>
```

Instead, use two separate `<script>` tags:

```html
<script src="file.js"></script>
<script>
  alert(1); // This will work
</script>
```


## 🧾 Summary

| Feature         | Syntax / Use Case                         |
|------------------|-------------------------------------------|
| Basic script     | `<script>alert('Hello')</script>`        |
| External script  | `<script src="script.js"></script>`       |
| Multiple scripts | Use multiple `<script>` tags              |
| Avoid            | `type="text/javascript"` and `language="javascript"` |


