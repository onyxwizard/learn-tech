# 📏 CSS Margin: Before & After Examples

This guide shows how each **CSS margin property** affects the spacing around an element — using clear before-and-after examples.

Each section explains a different `margin` behavior and demonstrates its visual impact on layout.
#### **Live view** : [CodePen📝](https://codepen.io/onyxwizard/pen/RNPMgbG)

## 🟠 `margin` (Shorthand)

Sets margins for all sides of an element in one line.

### 💡 Example:
```css
.margin-shorthand {
  margin: 20px;
}
```

#### ✅ Before:
```html
<div class="example default-margin">Default (no margin)</div>
```
- No extra space around the box

#### ✅ After:
```html
<div class="example margin-shorthand">All sides 20px</div>
<div class="example margin-vertical">Top & bottom 30px</div>
<div class="example margin-custom">Top 10px, Right 20px, Bottom 30px, Left 40px</div>
```

- `margin: 20px;` – Equal margin on all sides
- `margin: 30px 0;` – Vertical margin only (top and bottom)
- `margin: 10px 20px 30px 40px;` – Sets individual values clockwise: top → right → bottom → left

> 📌 Tip: Use shorthand to keep your CSS clean and readable.

## 🟢 Individual Margin Sides

Apply margin to just one side of an element.

### 💡 Example:
```css
.margin-top {
  margin-top: 20px;
}
```

#### ✅ Examples:
```html
<div class="example margin-top">Top margin of 20px</div>
<div class="example margin-right">Right margin of 20px</div>
<div class="example margin-bottom">Bottom margin of 40px</div>
<div class="example margin-left">Left margin of 30px</div>
```

- `margin-top`: Adds space above the box
- `margin-right`: Adds space to the right
- `margin-bottom`: Adds space below
- `margin-left`: Adds space to the left

> 📌 Tip: Useful when you need fine-grained control over spacing.

## 🟡 Margin Collapse Concept

When two vertical margins touch, they **collapse into a single margin**, equal to the **larger** of the two.

### 💡 Example:
```css
.collapse-box1 {
  margin-top: 50px;
}
.collapse-box2 {
  margin-top: 30px;
}
```

#### ✅ Result:
```html
<div class="example collapse-box1">Top margin 50px</div>
<div class="example collapse-box2">Top margin 30px</div>
```

- Total space between boxes = **50px**, not 80px
- This is called **margin collapse**

> 📌 Tip: Margin collapse only happens with **vertical** margins and doesn’t apply if:
> - The elements are floated or absolutely positioned
> - There's padding, border, or content between them

## 🎉 Summary Table

| Property | Purpose | Common Values |
|---|---|---|
| `margin` | Shorthand for setting all sides at once | `20px`, `30px 0`, `10px 20px 30px 40px` |
| `margin-top` | Top margin only | `20px` |
| `margin-right` | Right margin only | `20px` |
| `margin-bottom` | Bottom margin only | `40px` |
| `margin-left` | Left margin only | `30px` |
| Margin Collapse | When vertical margins combine | Only applies in specific conditions |

## 🧠 Final Thoughts

Understanding **CSS margin properties** helps you manage spacing and layout effectively. Whether you're arranging cards, aligning text, or designing full layouts:

* Use shorthand for simplicity
* Apply individual margins for precision
* Be aware of margin collapse when working with vertical spacing

Use this visual guide as a reference whenever you're working with margins in CSS!

📐✨ Happy Styling!