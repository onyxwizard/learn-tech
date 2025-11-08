# 🧠 CSS Selectors: Visual Guide

This document explains all types of **CSS selectors** using before-and-after examples. Each section demonstrates the selector's functionality and how it affects HTML elements.

#### **Live view** : [CodePen📝](https://codepen.io/onyxwizard/pen/yyNvzwR)

## 🧱 Simple Selectors

These are basic selectors used to target elements directly.

### 1. Element Selector (`p`)
```css
p.simple-example {
  background: lightblue;
}
```
- **🧠 Purpose:** Targets all `<p>` elements with the class `simple-example`.
- **📄 Example:**
  - Before: Normal paragraph 
  - After: Styled with `p.simple-example` 💡

### 2. Class Selector (`.class`)
```css
.my-class {
  font-weight: bold;
  color: green;
}
```
- **🧠 Purpose:** Targets any element with the class `my-class`.
- **📄 Example:**
  - Before: Regular div 
  - After: Styled with `.my-class` ✅

### 3. ID Selector (`#id`)
```css
#unique-id {
  border: 2px solid red;
  padding: 10px;
  background: #ffe0e0;
}
```
- **🧠 Purpose:** Targets the unique element with the ID `unique-id`.
- **📄 Example:**
  - Before: Regular div 
  - After: Styled with `#unique-id` 🔑

### 4. Universal Selector (`*`)
```css
* {
  margin: 0;
  padding: 0;
}
```
- **🧠 Purpose:** Applies styles to all elements on the page.
- **📄 Example:**
  - Every element gets default reset (already applied) 🌐

## 🔗 Combinator Selectors

These combine two or more selectors to define relationships between elements.

### 1. Descendant Selector (`div li`)
```css
.parent li {
  color: purple;
}
```
- **🧠 Purpose:** Styles all `<li>` elements inside a container with class `parent`, regardless of depth.
- **📄 Example:**
  ```html
  <ul class="parent">
    <li>Item 1</li>
    <li>Item 2</li>
  </ul>
  ```
  - All list items will be styled 📋

### 2. Child Selector (`div > li`)
```css
.parent > li {
  background: #f0f0f0;
}
```
- **🧠 Purpose:** Styles only direct child `<li>` elements of a parent with class `parent`.
- **📄 Example:**
  ```html
  <ul class="parent">
    <li>Direct child styled</li>
    <ul>
      <li>Not direct child</li>
    </ul>
  </ul>
  ```
  - Only the first `<li>` is styled 👶

### 3. Next Sibling Selector (`+`)
```css
.next-sibling + p {
  color: orange;
  font-weight: bold;
}
```
- **🧠 Purpose:** Styles the immediately following sibling `<p>` after an element with class `next-sibling`.
- **📄 Example:**
  ```html
  <div class="next-sibling">This is a sibling container</div>
  <p>After: Styled with .next-sibling + p</p>
  ```
  - The next `<p>` tag is styled 🚀

### 4. Subsequent Sibling Selector (`~`)
```css
.subsequent ~ p {
  border-left: 4px solid blue;
  padding-left: 10px;
}
```
- **🧠 Purpose:** Styles all subsequent sibling `<p>` elements after an element with class `subsequent`.
- **📄 Example:**
  ```html
  <div class="subsequent">Trigger div</div>
  <p>Styled by ~ p</p>
  <p>Also styled by ~ p</p>
  ```
  - Both `<p>` tags after the div are styled 📌

## 🪄 Pseudo-Class Selectors

Pseudo-classes allow you to style elements based on their state or position in the document tree.

### 1. `:hover`
```css
.hover:hover {
  background: yellow;
}
```
- **🧠 Purpose:** Styles an element when the user hovers over it.
- **📄 Example:**
  ```html
  <button class="hover">Hover over me!</button>
  ```
  - Button changes background on hover 🖱️

### 2. `:active`
```css
.active:active {
  background: red;
}
```
- **🧠 Purpose:** Styles an element while it’s being activated (e.g., clicked).
- **📄 Example:**
  ```html
  <button class="active">Click and hold</button>
  ```
  - Background turns red while button is pressed ⚠️

### 3. `:focus`
```css
.focus:focus {
  border: 2px solid green;
}
```
- **🧠 Purpose:** Styles an element when it gains focus (e.g., tabbed into or clicked).
- **📄 Example:**
  ```html
  <input class="focus" type="text" placeholder="Focus me">
  ```
  - Input field gets green border on focus 🔍

### 4. `:checked`
```css
input[type="checkbox"]:checked {
  accent-color: pink;
}
```
- **🧠 Purpose:** Styles checkboxes or radio buttons that are checked.
- **📄 Example:**
  ```html
  <input type="checkbox" checked> checkbox
  ```
  - Checkbox appears pink if supported 🟩

### 5. `:nth-child()`
```css
.nth-child:nth-child(odd) {
  background: #f0f0f0;
}
```
- **🧠 Purpose:** Selects every nth child of its parent (in this case, odd-numbered children).
- **📄 Example:**
  ```html
  <ul>
    <li class="nth-child">Item 1</li>
    <li class="nth-child">Item 2</li>
    <li class="nth-child">Item 3</li>
  </ul>
  ```
  - Items 1 and 3 have gray backgrounds 🔄

### 6. `:first-child`
```css
.first-child:first-child {
  font-weight: bold;
}
```
- **🧠 Purpose:** Styles the first child element among its siblings.
- **📄 Example:**
  ```html
  <ul>
    <li class="first-child">First item</li>
    <li>Second item</li>
  </ul>
  ```
  - First list item is bold 🥇

### 7. `:last-child`
```css
.last-child:last-child {
  color: red;
}
```
- **🧠 Purpose:** Styles the last child element among its siblings.
- **📄 Example:**
  ```html
  <ul>
    <li>First item</li>
    <li>Middle item</li>
    <li class="last-child">Last item</li>
  </ul>
  ```
  - Last list item is red 🥉

### 8. `:not()`
```css
.not-example:not(.highlighted) {
  opacity: 0.5;
}
```
- **🧠 Purpose:** Excludes elements matching the given selector.
- **📄 Example:**
  ```html
  <p class="not-example">Not highlighted</p>
  <p class="not-example highlighted">Highlighted</p>
  ```
  - First `<p>` is faded out ❌

### 9. `:required`
```css
.required:required {
  border: 2px solid red;
}
```
- **🧠 Purpose:** Styles form fields marked as required.
- **📄 Example:**
  ```html
  <input class="required" type="text" required>
  ```
  - Required input has red border 🛑

### 10. `:valid / :invalid`
```css
input:valid {
  background: #d4edda;
}

input:invalid {
  background: #f8d7da;
}
```
- **🧠 Purpose:** Styles inputs based on whether their content is valid according to their type.
- **📄 Example:**
  ```html
  <input type="email" placeholder="Type email">
  ```
  - Background changes based on validity ✅/❌

### 11. `:target`
```css
.target-example:target {
  background: yellow;
}
```
- **🧠 Purpose:** Styles the element whose ID matches the URL fragment identifier.
- **📄 Example:**
  ```html
  <a href="#section1">Scroll to Section</a>
  <p id="section1" class="target-example">Target Section</p>
  ```
  - Section highlights when scrolled to via link 🔗

### 12. `:visited`
```css
a:visited {
  color: purple;
}
```
- **🧠 Purpose:** Styles visited links differently.
- **📄 Example:**
  ```html
  <a href="#">Visited link</a>
  ```
  - Link becomes purple once visited 📘

### 13. `:disabled`
```css
input:disabled {
  background: #eee;
}
```
- **🧠 Purpose:** Styles disabled form elements.
- **📄 Example:**
  ```html
  <input type="text" disabled value="Disabled Input">
  ```
  - Input looks inactive 🔒

### 14. `:empty`
```css
.empty-div:empty {
  height: 30px;
  background: silver;
}
```
- **🧠 Purpose:** Styles empty elements.
- **📄 Example:**
  ```html
  <div class="empty-div"></div>
  ```
  - Empty div gets height and background 📦

### 15. `:default`
```css
.default:default {
  background: gold;
}
```
- **🧠 Purpose:** Styles the default option in a group (like a pre-checked radio button).
- **📄 Example:**
  ```html
  <input class="default" type="radio" name="opt" checked>
  ```
  - Radio button is gold if it’s default 🏆

### 16. `:placeholder-shown`
```css
.placeholder:placeholder-shown {
  background: #e6f7ff;
}
```
- **🧠 Purpose:** Styles input fields showing placeholder text.
- **📄 Example:**
  ```html
  <input type="text" placeholder="Enter your name">
  ```
  - Light blue background while placeholder is visible 📝

### 17. `:checked` (option)
```css
option:checked {
  background: lavender;
}
```
- **🧠 Purpose:** Styles selected options in a dropdown.
- **📄 Example:**
  ```html
  <select>
    <option>Option 1</option>
    <option selected>Selected Option</option>
  </select>
  ```
  - Selected option gets lavender background 🎯

## 💬 Pseudo-Elements Selectors

Pseudo-elements let you style specific parts of an element.

### 1. `::first-letter`
```css
.first-letter::first-letter {
  font-size: 2em;
  color: maroon;
}
```
- **🧠 Purpose:** Styles the first letter of a block-level element.
- **📄 Example:**
  ```html
  <p class="first-letter">This first letter will be styled.</p>
  ```
  - First letter is larger and colored maroon 📖

### 2. `::first-line`
```css
.first-line::first-line {
  color: gray;
}
```
- **🧠 Purpose:** Styles the first line of a paragraph.
- **📄 Example:**
  ```html
  <p class="first-line">The first line of this paragraph will be gray.</p>
  ```
  - First line is gray 📜

### 3. `::before / ::after`
```css
.add-content::before {
  content: "💡 ";
}

.add-content::after {
  content: " 🔚";
}
```
- **🧠 Purpose:** Inserts content before or after an element.
- **📄 Example:**
  ```html
  <p class="add-content">Icons are added before and after.</p>
  ```
  - Adds icons at start and end 📌

### 4. `::placeholder`
```css
.placeholder::placeholder {
  color: gray;
  font-style: italic;
}
```
- **🧠 Purpose:** Styles placeholder text inside an input.
- **📄 Example:**
  ```html
  <input class="placeholder" type="text" placeholder="Search...">
  ```
  - Placeholder is gray and italicized 🔍

### 5. `::marker`
```css
.marker li::marker {
  color: red;
  font-size: 1.2em;
}
```
- **🧠 Purpose:** Styles bullet points or numbers in lists.
- **📄 Example:**
  ```html
  <ul class="marker">
    <li>List Item 1</li>
    <li>List Item 2</li>
  </ul>
  ```
  - Bullets appear red and larger 🔴

### 6. `::selection`
```css
.selection::selection {
  background: yellow;
}
```
- **🧠 Purpose:** Styles text selected by the user.
- **📄 Example:**
  ```html
  <p class="selection">Select this text to see highlight style.</p>
  ```
  - Selected text gets yellow highlight 🖱️

## 🏷️ Attribute Selectors

Attribute selectors target elements based on their attributes and values.

### 1. `[attribute]`
```css
[target] {
  color: red;
}
```
- **🧠 Purpose:** Styles any element with the specified attribute.
- **📄 Example:**
  ```html
  <a target="_blank">Link with target attribute</a>
  ```
  - Link is red because it has `target` attribute 🔗

### 2. `[attribute=value]`
```css
[target="_blank"] {
  font-weight: bold;
}
```
- **🧠 Purpose:** Styles elements where the attribute exactly equals the value.
- **📄 Example:**
  ```html
  <a target="_blank">External link</a>
  ```
  - External link is bold 🔍

### 3. `[attribute~=value]`
```css
[title~="flower"] {
  color: green;
}
```
- **🧠 Purpose:** Styles elements where the attribute contains the word `flower`.
- **📄 Example:**
  ```html
  <p title="flower garden">Title contains flower</p>
  ```
  - Text turns green 🌸

### 4. `[attribute|=value]`
```css
[lang|="en"] {
  background: #e6f7ff;
}
```
- **🧠 Purpose:** Matches hyphenated language codes starting with the given value.
- **📄 Example:**
  ```html
  <p lang="en-US">Language is en-US</p>
  ```
  - Paragraph gets light blue background 🌍

### 5. `[attribute^=value]`
```css
a[href^="https"] {
  color: purple;
}
```
- **🧠 Purpose:** Styles elements where the attribute starts with the given value.
- **📄 Example:**
  ```html
  <a href="https://example.com">HTTPS Link</a>
  ```
  - HTTPS links are purple 🔐

### 6. `[attribute$=value]`
```css
a[href$=".pdf"] {
  color: brown;
}
```
- **🧠 Purpose:** Styles elements where the attribute ends with the given value.
- **📄 Example:**
  ```html
  <a href="document.pdf">PDF Link</a>
  ```
  - PDF links are brown 📄

### 7. `[attribute*=value]`
```css
a[href*="w3schools"] {
  font-weight: bold;
}
```
- **🧠 Purpose:** Styles elements where the attribute contains the value anywhere.
- **📄 Example:**
  ```html
  <a href="https://w3schools.com">W3Schools Link</a>
  ```
  - W3Schools links are bold 🎓

## 🎉 Conclusion

Understanding CSS selectors gives you precise control over styling and layout. This guide covers:
* **Basic element targeting**: Selecting elements directly by their tag name, class, or ID.
* **Relationship-based selection**: Using combinators to target elements based on their position relative to other elements.
* **State-based interactions**: Applying styles based on the user's interaction with an element (e.g., hover, focus, checked).
* **Customizing pseudo-elements**: Styling specific parts of an element, such as the first letter or line, or adding content before/after.
* **Fine-grained attribute filtering**: Targeting elements based on the presence, exact value, or partial value of their attributes.

With these tools, you can create highly targeted and responsive designs! 🎨✨