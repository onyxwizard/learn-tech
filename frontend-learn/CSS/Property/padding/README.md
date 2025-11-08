# 📦 CSS Padding: Before & After Examples

This guide shows how each **CSS padding property** affects the internal spacing of an element — using clear before-and-after examples.

Each section explains a different `padding` behavior and demonstrates its visual impact on layout and content positioning inside elements.

#### **Live view** : [CodePen📝](https://codepen.io/onyxwizard/pen/azOYwWz)

## 🟠 `padding` (Shorthand)

Sets padding for all sides of an element in one line.

### 💡 Example:
```css
.padding-shorthand {
  padding: 20px;
}
```

#### ✅ Before:
```html
<div class="example default-padding">Default (no padding)</div>
```
- Content touches the edges of the box

#### ✅ After:
```html
<div class="example padding-shorthand">All sides 20px</div>
<div class="example padding-vertical">Top & bottom 30px</div>
<div class="example padding-custom">Top 10px, Right 20px, Bottom 30px, Left 40px</div>
```

- `padding: 20px;` – Equal padding on all sides
- `padding: 30px 0;` – Vertical padding only (top and bottom)
- `padding: 10px 20px 30px 40px;` – Sets individual values clockwise: top → right → bottom → left

> 📌 Tip: Use shorthand to keep your CSS clean and readable.

## 🟢 Individual Padding Sides

Apply padding to just one side of an element.

### 💡 Example:
```css
.padding-top {
  padding-top: 20px;
}
```

#### ✅ Examples:
```html
<div class="example padding-top">Top padding of 20px</div>
<div class="example padding-right">Right padding of 20px</div>
<div class="example padding-bottom">Bottom padding of 40px</div>
<div class="example padding-left">Left padding of 30px</div>
```

- `padding-top`: Adds space above the content inside the box
- `padding-right`: Adds space to the right of the content
- `padding-bottom`: Adds space below the content
- `padding-left`: Adds space to the left of the content

> 📌 Tip: Useful when you need fine-grained control over internal spacing.

## 🎉 Summary Table

| Property | Purpose | Common Values |
|---|---|---|
| `padding` | Shorthand for setting all sides at once | `20px`, `30px 0`, `10px 20px 30px 40px` |
| `padding-top` | Top padding only | `20px` |
| `padding-right` | Right padding only | `20px` |
| `padding-bottom` | Bottom padding only | `40px` |
| `padding-left` | Left padding only | `30px` |

## 🧠 Final Thoughts

Understanding **CSS padding properties** helps you manage internal spacing within elements effectively. Whether you're designing buttons, cards, or layout containers:

* Use shorthand for simplicity and consistency
* Apply individual paddings for precise adjustments
* Remember that padding increases the total size of an element unless **`box-sizing: border-box`** is used

Use this visual guide as a reference whenever you're working with padding in CSS!

🧱✨ Happy Styling!