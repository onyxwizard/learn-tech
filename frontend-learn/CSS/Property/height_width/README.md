# 📐 CSS Height & Width: Before & After Examples

This guide shows how each **CSS height and width property** affects the size of an element — using clear before-and-after examples.

Each section demonstrates a different dimension-related CSS property and its visual effect on layout and content behavior.

#### **Live view** : [CodePen📝](https://codepen.io/onyxwizard/pen/RNPMgdx)
## 🟠 `width`

Controls the **horizontal size** of an element.

### 💡 Example:
```css
.fixed-width {
  width: 200px;
}
```

#### ✅ Before:
```html
<div class="example default-size">Default (auto)</div>
```
- Width is automatically adjusted based on parent/container

#### ✅ After:
```html
<div class="example fixed-width">Fixed width: 200px</div>
<div class="example percent-width">Width in percentage: 50%</div>
```

- `width: 200px;` – Sets a fixed pixel-based width
- `width: 50%;` – Makes the box take up half of the parent container’s width

> 📌 Tip: Use `%` for responsive layouts and `px` when you need exact control.

## 🟢 `height`

Controls the **vertical size** of an element.

### 💡 Example:
```css
.fixed-height {
  height: 100px;
}
```

#### ✅ Before:
```html
<div class="example default-size">Default (auto)</div>
```
- Height adjusts automatically to fit content

#### ✅ After:
```html
<div class="example fixed-height">Fixed height: 100px</div>
<div class="example small-height">Smaller height: 50px</div>
```

- `height: 100px;` – Sets a fixed vertical size
- `height: 50px;` – Makes the box shorter vertically

> 📌 Tip: Always consider content overflow when setting fixed heights.

## 🟡 `min-width`

Ensures that an element doesn’t shrink below a certain width.

### 💡 Example:
```css
.min-width {
  min-width: 150px;
}
```

#### ✅ Example:
```html
<div class="example min-width">Minimum width: 150px</div>
```

- Even if you try to set `width: 100px`, it will still be at least `150px` wide

> 📌 Tip: Great for preventing layout collapse on smaller screens.

## 🔵 `max-width`

Limits how wide an element can grow.

### 💡 Example:
```css
.max-width {
  max-width: 300px;
}
```

#### ✅ Example:
```html
<div class="example max-width">Maximum width: 300px</div>
```

- Even with `width: 400px`, it will only expand to `300px`

> 📌 Tip: Ideal for responsive images or containers that shouldn't stretch too far.

## 🟣 `min-height`

Ensures that an element doesn’t get shorter than a minimum height.

### 💡 Example:
```css
.min-height {
  min-height: 80px;
}
```

#### ✅ Example:
```html
<div class="example min-height">Minimum height: 80px</div>
```

- Even if you set `height: 40px`, the actual height will be `80px`

> 📌 Tip: Useful for ensuring consistent spacing inside cards or boxes.

## 🟥 `max-height`

Limits how tall an element can become.

### 💡 Example:
```css
.max-height {
  max-height: 200px;
}
```

#### ✅ Example:
```html
<div class="example max-height">Maximum height: 200px</div>
```

- If `height: 300px` is set, it will be clipped to `200px` due to `max-height`
- Combined with `overflow: hidden`, this creates a scrollable or cropped area

> 📌 Tip: Often used with dropdowns, tooltips, or collapsible sections.

## 🎉 Summary Table

| Property | Purpose | Common Values |
|---|---|---|
| `width` | Sets the width of an element | `200px`, `50%`, `auto` |
| `height` | Sets the height of an element | `100px`, `50px` |
| `min-width` | Prevents element from being narrower | `150px` |
| `max-width` | Prevents element from being wider | `300px` |
| `min-height` | Prevents element from being shorter | `80px` |
| `max-height` | Prevents element from being taller | `200px` |

## 🧠 Final Thoughts

Understanding **CSS height and width properties** gives you full control over how your elements behave across screen sizes and content variations. Whether you're designing cards, modals, or full-page layouts:

* Use **`width`** and **`height`** for precise sizing
* Add **`min-width`**/**`min-height`** to prevent unwanted shrinking
* Set **`max-width`**/**`max-height`** to limit growth and avoid overflow issues

Use this visual guide as a reference whenever you're working with dimensions in CSS!

📏✨ Happy Styling!