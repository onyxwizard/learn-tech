# 🧱 CSS Borders: Before & After Examples

This guide shows how each **CSS border property** changes the appearance of an element — using clear before-and-after examples.

Each section demonstrates a different border-related CSS property and its visual effect, making it easy to understand and apply in your own projects.

#### **Live view** : [CodePen📝](https://codepen.io/onyxwizard/pen/ZYGrveL)

## 🟠 `border-style`

Controls the **type of line** used for borders. Without setting a style, no border will appear.

### 💡 Example:
```css
.dotted {
  border-style: dotted;
  border-color: black;
}
```

#### ✅ Before:
```html
<div class="example default-border">Default (no border)</div>
```
- No visible border

#### ✅ After:
```html
<div class="example dotted">dotted</div>
<div class="example dashed">dashed</div>
<div class="example solid">solid</div>
<div class="example double">double</div>
<div class="example groove">groove</div>
<div class="example ridge">ridge</div>
<div class="example inset">inset</div>
<div class="example outset">outset</div>
<div class="example none">none</div>
<div class="example hidden">hidden</div>
```

- `dotted`: Dots around the box
- `dashed`: Dashes
- `solid`: Continuous line
- `double`: Two lines
- `groove`, `ridge`, `inset`, `outset`: 3D effects based on color
- `none` / `hidden`: Removes border

> 📌 Tip: Use `hidden` with tables; `none` is more common for general elements.

## 🟢 `border-width`

Sets the **thickness** of the border.

### 💡 Example:
```css
.medium {
  border-width: 5px;
}
```

#### ✅ Examples:
```html
<div class="example thin">Thin border</div>
<div class="example medium">Medium border</div>
<div class="example thick">Thick border</div>
<div class="example custom-width">Custom width</div>
```

- `thin`, `medium`, `thick`: Keyword values
- Custom: You can define different widths per side like:
  ```css
  border-width: 4px 8px 12px 16px;
  ```

> 📌 Tip: Combine with `border-style` and `border-color` for full control.

## 🟡 `border-color`

Defines the **color** of the border.

### 💡 Example:
```css
.red {
  border-color: red;
}
```

#### ✅ Examples:
```html
<div class="example red">Red border</div>
<div class="example hex">HEX color</div>
<div class="example rgb">RGB color</div>
<div class="example hsl">HSL color</div>
```

- `red`: Named color
- `#007BFF`: HEX code
- `rgb(0, 128, 0)`: RGB value
- `hsl(120, 100%, 25%)`: HSL format

> 📌 Tip: Always set a `border-style` first—otherwise, the color won’t show.

## 🔵 Individual Side Styles

Apply border styles to specific sides only.

### 💡 Example:
```css
.top-dashed {
  border-top-style: dashed;
}
```

#### ✅ Examples:
```html
<div class="example top-dashed">Top dashed</div>
<div class="example right-dashed">Right dashed</div>
<div class="example bottom-dashed">Bottom dashed</div>
<div class="example left-dashed">Left dashed</div>
```

- `border-top-style`: Top border only
- `border-right-style`: Right border only
- `border-bottom-style`: Bottom border only
- `border-left-style`: Left border only

> 📌 Tip: Useful for dividers or directional emphasis.

## 🟣 `border` (Shorthand)

A shorthand property that combines `border-width`, `border-style`, and `border-color`.

### 💡 Example:
```css
.shorthand-solid {
  border: 2px solid blue;
}
```

#### ✅ Examples:
```html
<div class="example shorthand-solid">Solid border</div>
<div class="example shorthand-dashed">Dashed border</div>
```

- `2px solid blue`: Sets all three properties in one line
- `3px dashed green`: Applies dashed style with green color

> 📌 Tip: Order doesn't matter, but best practice is: `width style color`.

## 🟥 `border-radius`

Rounds the corners of an element’s border.

### 💡 Example:
```css
.round-medium {
  border-radius: 25px;
}
```

#### ✅ Examples:
```html
<div class="example round-small">Small rounded corners</div>
<div class="example round-medium">Medium rounded corners</div>
<div class="example round-large">Large rounded corners</div>
<div class="example round-custom">Custom radius</div>
```

- `10px`: Slight rounding
- `25px`: More noticeable curve
- `50%`: Turns into a circle if the element is square
- `30px 10px`: Different horizontal and vertical radii

> 📌 Tip: Use `50%` to create pill-shaped buttons or circular avatars.

## 🎉 Summary Table

| Property | Purpose | Common Values |
|---|---|---|
| `border-style` | Type of border line | `solid`, `dashed`, `dotted`, `none` |
| `border-width` | Thickness of the border | `1px`, `5px`, `thin`, `thick` |
| `border-color` | Color of the border | `red`, `#007bff`, `rgb()`, `hsl()` |
| `border-top-style` etc. | Border style for individual sides | `dashed`, `solid`, `none` |
| `border` | Shorthand for width, style, color | `2px solid #000` |
| `border-radius` | Rounds corner edges | `10px`, `50%`, `30px 10px` |

## 🧠 Final Thoughts

Understanding **CSS border properties** gives you precise control over the visual boundaries of your elements. Whether you're designing cards, buttons, or layout containers, these properties help:

* Add definition and structure
* Create soft or bold visual effects
* Design modern UI components like pills, circles, and shadows

Use this visual guide as a reference whenever you're working with borders in CSS!

🖌️✨ Happy Styling!