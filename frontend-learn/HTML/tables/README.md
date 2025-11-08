## 🧱 `<colgroup>` & `span` – Column Styling Made Visual 🎨

### 🔹 What is `<colgroup>`?

- It’s an HTML tag used to **group table columns** together.
- You can style multiple columns at once without applying styles to every cell (`<td>` or `<th>`).

### 🔸 What is `span`?

- The `span` attribute inside `<col>` tells **how many columns** the current style should apply to.


## 📊 Explanation

Imagine your table has 4 columns:

```
| Col 1 | Col 2 | Col 3 | Col 4 |
```

Now look at this `<colgroup>` setup:

```html
<colgroup>
  <col span="1" style="background-color: red">
  <col span="3" style="background-color: lightblue">
</colgroup>
```

This means:

| Column | Style Applied By       | Background Color |
|--------|------------------------|------------------|
| Col 1  | First `<col>`          | ❗ Red           |
| Col 2  | Second `<col>`         | 🟦 Light Blue    |
| Col 3  | Second `<col>`         | 🟦 Light Blue    |
| Col 4  | Second `<col>`         | 🟦 Light Blue    |

So:
- ✅ One column gets red background.
- ✅ Next three columns get light blue background.


## 🧠 Key Takeaways

- 🧩 `<colgroup>` helps you **style columns in groups**
- ➕ `span="n"` means “apply this style to **n columns** starting from the current one”
- 🌈 Works best for simple styles like `width`, `background-color`
- ⚠️ Not all CSS properties work (like `padding`, `margin`)
- 📏 Don’t use `width: 100%` on multiple columns — it doesn't add up!
- ✅ Use for consistent layouts, readability, and performance


## 📝 Final Example You Can Try

```html
<table border="1">
  <colgroup>
    <col span="1" style="background-color: #ffcccc; width: 30%">
    <col span="2" style="background-color: #ccffcc; width: 20%">
    <col span="1" style="background-color: #ccccff; width: 30%">
  </colgroup>

  <tr>
    <th>Col 1</th>
    <th>Col 2</th>
    <th>Col 3</th>
    <th>Col 4</th>
  </tr>
  <tr>
    <td>Data A1</td>
    <td>Data A2</td>
    <td>Data A3</td>
    <td>Data A4</td>
  </tr>
</table>
```

## 🎯 TL;DR Summary

```
<colgroup> 📦 = Group & style columns
<col> 🎨 = Define style for 1+ columns
span="n" ➕ = Apply style to "n" columns
🎨 Works with: width, background-color
🚫 Not for: padding, borders, complex styles
```
