# 🎨 07 — Static Assets Layer (CSS & JavaScript)

## 🎯 Purpose of This Layer
Contains the presentation design system, responsive styles, animations, Chart.js configuration, and client-side form validation scripts.

---

## 📂 Files in This Folder

### 1. `css/style.css`
* **Purpose**: Modern CSS3 styling system.
* **Design System**:
  * CSS Variables (`--primary: #4f46e5`, `--accent: #06b6d4`, `--success: #10b981`, `--warning: #f59e0b`, `--danger: #ef4444`, `--sidebar-bg: #0f172a`).
  * Responsive layout: Desktop fixed sidebar, mobile slide-out navbar toggle.
  * KPI summary cards with hover transitions and indicator stripes.
  * Category badge pill styling with customized color themes for each category.
  * Clean form controls with focus rings and icon overlays.
  * Print-ready styles (`@media print`) that hide navigation, buttons, and extra controls for clean PDF/paper reports.

---

### 2. `js/main.js`
* **Purpose**: Interactive client-side behaviors and chart rendering.
* **Key Functions**:
  * **Mobile Navigation**: Toggles mobile sidebar menu on small screens and closes on outside click.
  * **Chart.js Initializer**:
    * `initCategoryChart()`: Renders interactive Doughnut Chart with responsive cutout, tooltips, and custom colors.
    * `initTrendChart()`: Renders daily spending Bar Chart with custom axes and currency formatting.
  * **Client Form Validation**: Enforces password matching on registration and positive amount checks on expense submission.
  * `confirmDelete(description, amount)`: Displays browser confirmation alert before submitting expense deletion forms.

---

## 📐 Icon Breathing Room Changes

Icons throughout the UI were updated for better visual spacing and readability.

### KPI Card Icons (`.kpi-icon-wrapper`)
| Property | Before | After |
|----------|--------|-------|
| width/height | 48px | 58px |
| font-size | 1.5rem | 1.7rem |

### Tinted Icon Backgrounds
Added color-coded backgrounds to KPI icon wrappers:
- `.card-primary` → `#eef2ff` (light indigo)
- `.card-info` → `#eff6ff` (light blue)
- `.card-success` → `#ecfdf5` (light green)
- `.card-warning` → `#fffbeb` (light amber)
- `.card-danger` → `#fef2f2` (light red)
- `.card-purple` → `#f5f3ff` (light purple)

### Table Action Buttons (`.btn-icon-action`)
| Property | Before | After |
|----------|--------|-------|
| width/height | 32px | 38px |
| font-size | 0.9rem | 1.05rem |

### Sidebar Nav Icons (`.nav-icon`)
| Property | Before | After |
|----------|--------|-------|
| font-size | 1.15rem | 1.35rem |

### Spacing Adjustments
| Selector | Property | Before | After |
|----------|----------|--------|-------|
| `.kpi-card` | gap | 1.25rem | 1.5rem |
| `.nav-link` | gap | 0.85rem | 1rem |

### Input Field Icons (`.input-icon`)
| Property | Before | After |
|----------|--------|-------|
| left | 0.85rem | 1.5rem |
| font-size | 1rem | 1.2rem |

### Input Field Padding (`.input-wrapper input`)
| Property | Before | After |
|----------|--------|-------|
| padding-left | 2.5rem | 4rem |
