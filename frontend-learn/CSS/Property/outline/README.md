# 🖋️ CSS Outline: Before & After Examples

The **CSS `outline`** property is used to draw a line around an element, outside of any borders. It's often used for accessibility (e.g., focus indicators) and visual effects.

This guide visually explains how each part of the `outline` system affects the appearance of an element.

#### **Live view** : [CodePen📝](https://codepen.io/onyxwizard/pen/raVdzZm)

## 🟠 `outline-style`

Defines the **type of line** used for the outline.

### 💡 Example:
```css
.solid {
  outline-style: solid;
}
```

#### ✅ Before:
```html
<div class="example default-outline">Default (no outline)</div>
```
- No visible outline

#### ✅ After:
```html
<div class="example solid">solid</div>
<div class="example dotted">dotted</div>
<div class="example dashed">dashed</div>
<div class="example double">double</div>
```

- `solid`: A continuous line
- `dotted`: Dots form the line
- `dashed`: Dashes form the line
- `double`: Two parallel lines

> 📌 Tip: Unlike borders, outlines do not take up space in the layout.

## 🟢 `outline-color`

Sets the **color** of the outline.

### 💡 Example:
```css
.red {
  outline-color: red;
}
```

#### ✅ Examples:
```html
<div class="example red">Red outline</div>
<div class="example hex">HEX color</div>
<div class="example rgb">RGB color</div>
<div class="example invert">invert</div>
```

- `red`: Named color
- `#00ff00`: HEX value (green)
- `rgb(0, 0, 255)`: RGB format (blue)
- `invert`: Makes the outline clearly visible on any background

> 📌 Tip: Use `invert` for accessible focus states that work on any background.

## 🟡 `outline-width`

Controls the **thickness** of the outline.

### 💡 Example:
```css
.medium {
  outline-width: 3px;
}
```

#### ✅ Examples:
```html
<div class="example thin">Thin outline</div>
<div class="example medium">Medium outline</div>
<div class="example thick">Thick outline</div>
```

- `thin`: Usually about `1px`
- `medium`: Default width (~2px)
- `thick`: Around `5px` or more

> 📌 Tip: Outline width doesn’t affect layout flow like border width does.

## 🔵 `outline-offset`

Sets the **distance between the outline and the element’s border**.

### 💡 Example:
```css
.offset {
  outline-offset: 5px;
}
```

#### ✅ Examples:
```html
<div class="example no-offset">No offset</div>
<div class="example offset">Offset by 5px</div>
```

- `no-offset`: Outline touches the element’s edge
- `offset`: Outline is spaced away from the element by `5px`

> 📌 Tip: Great for improving visibility without overlapping other elements.

## 🟣 `outline` (Shorthand)

A shorthand property that combines `outline-width`, `outline-style`, and `outline-color`.

### 💡 Example:
```css
.shorthand-solid {
  outline: 2px solid blue;
}
```

#### ✅ Examples:
```html
<div class="example shorthand-solid">Solid outline</div>
<div class="example shorthand-dashed">Dashed outline</div>
```

- `2px solid blue`: Sets all three properties in one line
- `3px dashed orange`: Applies dashed style with orange color

> 📌 Tip: Order doesn't matter much, but best practice is: `width style color`.

## 🎉 Summary Table

| Property           | Purpose                                | Common Values                          |
|--------------------|----------------------------------------|----------------------------------------|
| `outline-style`    | Type of outline line                   | `solid`, `dashed`, `dotted`, `double`  |
| `outline-color`    | Color of the outline                   | `red`, `#00ff00`, `rgb()`, `invert`    |
| `outline-width`    | Thickness of the outline               | `1px`, `3px`, `thin`, `thick`          |
| `outline-offset`   | Space between outline and border       | `5px`, `10px`                          |
| `outline`          | Shorthand for width, style, and color  | `2px solid #000`                       |

## 🧠 Final Thoughts

Understanding **CSS outline properties** helps you create accessible and visually distinct elements — especially for interactive components like buttons and links.

- Use it for **focus indicators** to improve accessibility
- Combine with `:focus` or `:hover` for better UX
- Remember that outlines don’t affect layout like borders

Use this visual guide whenever you're working with outlines in CSS!

🖋️✨ Happy Styling!