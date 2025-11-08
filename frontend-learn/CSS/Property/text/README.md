# 📝 CSS Text: Styling Guide 🎨

This guide explains how to **style text using CSS**, covering alignment, decoration, transformation, spacing, and shadows. Each section includes examples and visual explanations of key properties.

Get ready for a fun and interactive journey through the world of text styling! 🚀

#### **Live view** : [CodePen📝](https://codepen.io/onyxwizard/pen/bNdvYMj)

## 🧱 Text Alignment & Direction 🧭

### `text-align` 🎯
Sets the horizontal alignment of text.

| Value | Description |
|---|---|
| `left` | Aligns text to the left (default) 👈 |
| `right` | Aligns text to the right 👉 |
| `center` | Centers text 🧊 |
| `justify` | Stretches lines to equal width 📏 |

#### Example:
```css
h1 {
  text-align: center;
}
```

### `text-align-last` 📐
Controls alignment of the **last line** in a block.

| Value | Description |
|---|---|
| `auto` | Default alignment 🤷‍♂️ |
| `left` | Last line aligns left 👈 |
| `right` | Last line aligns right 👉 |
| `center` | Last line centers 🧊 |
| `justify` | Last line stretches 📐 |

#### Example:
```css
p.a {
  text-align-last: right;
}
```

### `direction` ↔️
Specifies the **text direction**.

| Value | Description |
|---|---|
| `ltr` | Left-to-right (default) ←️ |
| `rtl` | Right-to-left →️ |

#### Example:
```css
p {
  direction: rtl;
}
```

### `unicode-bidi` 🔁
Used with `direction` to handle bidirectional text.

| Value | Description |
|---|---|
| `normal` | Default behavior 🔄 |
| `bidi-override` | Forces text to display LTR or RTL 💥 |

#### Example:
```css
p {
  direction: rtl;
  unicode-bidi: bidi-override;
}
```

### `vertical-align` ⬆️⬇️
Sets vertical alignment of inline or table-cell elements.

| Value | Description |
|---|---|
| `baseline` | Aligns to baseline (default) 📐 |
| `top` | Aligns to top 📛 |
| `middle` | Vertically centered 🧊 |
| `bottom` | Aligns to bottom 📙 |
| `sub`, `super` | Subscript/superscript ₜ/ᵀ |
| `text-top`, `text-bottom` | Relative to text 📖 |

#### Example:
```css
img.b {
  vertical-align: text-top;
}
```

## ✏️ Text Decoration 🎨

### `text-decoration-line` 🌟
Adds underline, overline, or line-through.

| Value | Description |
|---|---|
| `none` | No decoration ❌ |
| `underline` | Underlines text ✍️ |
| `overline` | Overlines text 📝 |
| `line-through` | Strikes through text 🚫 |

#### Example:
```css
h2 {
  text-decoration-line: line-through;
}
```

### `text-decoration-color` 🎨
Sets the color of the decoration line.

| Value | Description |
|---|---|
| `red` | Red underline ❤️ |
| `#00ff00` | Green from HEX 🟢 |
| `rgb()` | Custom color 🎨 |

#### Example:
```css
h3 {
  text-decoration-color: green;
}
```

### `text-decoration-style` 🖌️
Sets style of the decoration line.

| Value | Description |
|---|---|
| `solid` | Solid line —— |
| `dotted` | Dotted line ⬤ ⬤ ⬤ |
| `dashed` | Dashed line – – – |
| `double` | Double line = = = |
| `wavy` | Wavy line ~ ~ ~ 💡 |

#### Example:
```css
p.ex2 {
  text-decoration-style: wavy;
}
```

### `text-decoration-thickness` 📏
Controls the thickness of the underline.

| Value | Description |
|---|---|
| `auto` | Default thickness 📏 |
| `5px` | Thicker line 📐 |
| `25%` | Percentage-based thickness % |

#### Example:
```css
h2 {
  text-decoration-thickness: 5px;
}
```

### `text-decoration` 🎗️ *(Shorthand)*
Combines all decoration properties into one line.

#### Example:
```css
p {
  text-decoration: underline red double 5px;
}
```

## 🔠 Text Transformation 📞

### `text-transform`
Changes case of letters.

| Value | Description |
|---|---|
| `uppercase` | All caps 🅐🅑🅒 |
| `lowercase` | All lowercase abc |
| `capitalize` | First letter capitalized 🐫 |

#### Example:
```css
p.capitalize {
  text-transform: capitalize;
}
```

## 📏 Text Spacing 🪵

### `text-indent` 🧩
Indents the first line of a paragraph.

#### Example:
```css
p {
  text-indent: 50px;
}
```

### `letter-spacing` 🧮
Adjusts space between characters.

| Value | Description |
|---|---|
| `5px` | Wider spacing 🕳️ |
| `-2px` | Tighter spacing 🧩 |

#### Example:
```css
h1 {
  letter-spacing: 5px;
}
```

### `word-spacing` 🧩
Adjusts space between words.

| Value | Description |
|---|---|
| `10px` | More spacing 📦 |
| `-2px` | Less spacing 🧸 |

#### Example:
```css
p.two {
  word-spacing: -2px;
}
```

### `line-height` 📏
Controls spacing between lines.

| Value | Description |
|---|---|
| `0.8` | Compact lines 📚 |
| `1.8` | Spacious lines 📖 |

#### Example:
```css
p.big {
  line-height: 1.8;
}
```

### `white-space` 🧻
Controls how whitespace is handled.

| Value | Description |
|---|---|
| `normal` | Normal spacing (default) |
| `nowrap` | No wrapping 🚫 |
| `pre` | Preserves spaces/tabs 📄 |

#### Example:
```css
p {
  white-space: nowrap;
}
```

## 🌟 Text Shadow 🌑

### `text-shadow` 🌌
Adds shadow to text.

#### Example:
```css
h1 {
  text-shadow: 2px 2px 5px red;
}
```

### Multiple Shadows 💥
You can add multiple shadows separated by commas!

#### Example:
```css
h1 {
  text-shadow: 0 0 3px #ff0000, 0 0 5px #0000ff;
}
```

## 🎯 Summary Table

| Property | Use Case |
|---|---|
| `text-align` | Horizontal alignment 🎯 |
| `text-align-last` | Last line alignment 🧩 |
| `direction` / `unicode-bidi` | Change text flow ↔️ |
| `vertical-align` | Vertical position in inline/table cells ⬆️⬇️ |
| `text-decoration` | Add/remove underlines/strikethrough 🎨 |
| `text-transform` | Uppercase/lowercase control 🔠 |
| `letter-spacing` / `word-spacing` | Adjust character/word spacing 🧮 |
| `line-height` | Line spacing control 📏 |
| `white-space` | Controls text wrapping 🧻 |
| `text-shadow` | Add glow/shadow to text 🌌 |

## 🧠 Final Thoughts

Mastering **CSS text properties** lets you create clean, readable, and visually appealing content across your website.

Use this guide as a quick reference when styling text:

* Improve readability with proper spacing and alignment
* Make links clear without confusing users
* Add flair with shadows and transformations
* Support multilingual layouts with `direction` and `unicode-bidi`

Now go make your text shine like never before! ✨🖋️📘

Happy Styling! 💻🎨