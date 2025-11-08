# 📦 CSS Box Model: Before & After Examples

The **CSS box model** is the foundation of layout in CSS. Every HTML element is represented as a rectangular box, and each box consists of:

- Content
- Padding
- Border
- Margin

This guide visually explains how each part affects the final size and spacing of an element.

#### **Live view** : [CodePen📝](https://codepen.io/onyxwizard/pen/ByNrdjv)
## 🟠 `width`

Defines the **content width** of the box — not including padding or border.

### 💡 Example:
```css
.set-width {
  width: 200px;
}
```

#### ✅ Before:
```html
<div class="example default-size">Default (auto)</div>
```
- Width adjusts to fit parent container

#### ✅ After:
```html
<div class="example set-width">Set to 200px</div>
```
- Content area is exactly `200px` wide
- Padding and borders are added outside this width by default

> 📌 Tip: Always consider total width when using padding and borders.

## 🟢 `height`

Sets the **content height** of the box — not including padding or border.

### 💡 Example:
```css
.set-height {
  height: 100px;
}
```

#### ✅ Before:
```html
<div class="example default-size">Default (auto)</div>
```
- Height adjusts automatically based on content

#### ✅ After:
```html
<div class="example set-height">Set to 100px</div>
```
- Content area is fixed at `100px` tall
- Padding and borders increase the total visible height

> 📌 Tip: Use `box-sizing: border-box` to include padding and border in width/height.

## 🟡 `padding`

Adds **internal space** between the content and the border.

### 💡 Example:
```css
.set-padding {
  padding: 20px;
}
```

#### ✅ Before:
```html
<div class="example default-size">No padding</div>
```
- Content touches edges

#### ✅ After:
```html
<div class="example set-padding">Padding 20px</div>
```
- Content is pushed inward with `20px` padding around it
- Total width = content width + left/right padding
- Total height = content height + top/bottom padding

> 📌 Tip: Padding adds visual breathing room and affects background color/texture.

## 🔵 `border`

Draws a line **around the content and padding**.

### 💡 Example:
```css
.set-border {
  border: 5px solid black;
}
```

#### ✅ Before:
```html
<div class="example default-size">No border</div>
```
- No visible boundary

#### ✅ After:
```html
<div class="example set-border">Border 5px solid black</div>
```
- Adds a solid black border that wraps around content and padding
- Border increases the overall size unless `box-sizing: border-box` is used

> 📌 Tip: The border contributes to the box's visual weight and structure.

## 🟣 `margin`

Controls **external spacing**, pushing other elements away from the box.

### 💡 Example:
```css
.set-margin {
  margin: 30px;
}
```

#### ✅ Before:
```html
<div class="example default-margin">Default margin</div>
```
- Default spacing between elements

#### ✅ After:
```html
<div class="example set-margin">Margin 30px</div>
```
- Adds 30px of empty space around the box
- Does not affect the box’s own size, only its position relative to others

> 📌 Tip: Margins can collapse vertically if two elements touch.

## 🟥 Full Box Model Example

Putting all parts together: **content**, **padding**, **border**, and **margin**.

### 💡 Example:
```css
.full-box {
  width: 200px;
  height: 100px;
  padding: 20px;
  border: 5px solid black;
  margin: 30px;
}
```

#### ✅ Visual Result:
```html
<div class="example full-box">With padding, border, and margin</div>
```

#### ✅ Size Calculation (with default `box-sizing: content-box`):
- **Total width** = 200px (content) + 40px (left + right padding) + 10px (left + right border) = **250px**
- **Total height** = 100px (content) + 40px (top + bottom padding) + 10px (top + bottom border) = **150px**
- **Margin** = Adds external spacing but does not change box size

> 📌 Tip: Use `box-sizing: border-box` to simplify sizing calculations.

## 🎉 Summary Table

| Part      | Purpose                               | Affects Size? | Included in `box-sizing: border-box`? |
|-----------|----------------------------------------|----------------|----------------------------------------|
| `width`   | Content width                          | ✅ Yes         | ✅ Yes                                 |
| `height`  | Content height                         | ✅ Yes         | ✅ Yes                                 |
| `padding` | Internal spacing                       | ✅ Yes         | ✅ Yes                                 |
| `border`  | Border around content + padding        | ✅ Yes         | ✅ Yes                                 |
| `margin`  | External spacing                       | ❌ No          | ❌ No                                  |

## 🧠 Final Thoughts

Understanding the **CSS Box Model** is essential for mastering layout and spacing in web design. Each part plays a role in how your elements appear and interact with one another.

- Know how `width`, `height`, `padding`, `border`, and `margin` contribute to the total size
- Use `box-sizing: border-box` to simplify sizing
- Be mindful of margin collapse and internal spacing

Use this visual guide whenever you're debugging layout issues or designing responsive components!

🧱✨ Happy Styling!