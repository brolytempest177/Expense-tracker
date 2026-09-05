# Product

<!-- impeccable:product-schema 1 -->

## Platform

web

## Users

Primary users are **students, young professionals, and early-career individuals** managing personal finances on strict monthly budgets. They need to track daily recurring expenditures (groceries, commute, rent, course materials, leisure) and prevent end-of-month budget deficits.

Secondary users include **academic evaluators and instructors** reviewing the full-stack software architecture, data integrity, and security design during viva examinations.

## Product Purpose

To provide a disciplined, transparent personal finance tracking application that actively helps users stay solvent. Success means a user knows their exact daily balance in under three seconds, receives clear visual warnings before exhausting their monthly limit, and maintains positive net savings.

## Positioning

Unlike complex commercial accounting software or ad-heavy freemium mobile apps, ExpenseTracker is a clean, self-hosted, private web dashboard focused squarely on **proactive monthly budget enforcement** and rapid transaction logging without visual clutter.

## Operating Context

- **Daily Logging**: Quick entry of expenses on mobile or desktop right after transactions occur.
- **Monthly Review**: Checking category spending distributions, identifying non-essential leaks, and resetting the budget for the upcoming month.
- **Academic Evaluation**: Demonstrating clean MVC architecture, relational database integrity (MySQL with HikariCP), and parameterized JDBC security against SQL injection.

## Capabilities and Constraints

- **Strict User Isolation**: Every transaction, budget limit, and report is securely scoped to the authenticated `user_id`.
- **Proactive Budget Discipline**: Automated real-time calculations tracking Allocated Budget, Total Spent, and Remaining Balance with dynamic status indicators:
  - *On Track*: Spending $\le 80\%$ of budget limit.
  - *Near Limit*: Spending between $80\%$ and $100\%$ with amber warning.
  - *Exceeded*: Spending $> 100\%$ with immediate rose alert and negative balance readout.
- **Transaction Lifecycle**: Full CRUD operations with standardized categories (*Food, Transport, Education, Shopping, Entertainment, Bills, Health, Other*), multi-parameter search, and chronological sorting.
- **Data Visualizations**: High-density interactive Chart.js doughnut breakdowns, daily transaction bar trends, and print-ready summary reports.
- **Multi-Currency Requirement**: Configurable currency symbols supporting **₹ (INR)**, **$ (USD)**, **€ (EUR)**, and **£ (GBP)**.
- **Technical Architecture**: Java 11+, Java Servlets 4.0, JSP 2.3 + JSTL 1.2, Apache Tomcat 9, MySQL 8.0+ with HikariCP connection pooling, BCrypt password hashing.

## Brand Commitments

- **Name**: ExpenseTracker
- **Tone & Voice**: Grounded, authoritative, disciplined, and financially transparent.
- **Visual Identity**: High-craft deep emerald teal and slate palette, geometric Manrope typography, tabular numeral alignment, and clean elevated surfaces.

## Product Principles

1. **Discipline over Passive Recording**: A finance tracker must actively warn the user of impending financial stress rather than merely graphing historical mistakes.
2. **Numerical Precision & Clarity**: All monetary values must use tabular figures with consistent decimal formatting; financial data must never jitter or misalign.
3. **Frictionless Entry**: The distance between logging in and saving an expense should be under three seconds with minimal required fields.
4. **Resilient Privacy & Security**: Zero cross-user data leakage with salted BCrypt password security and parameterized database queries.

## Accessibility & Inclusion

- Adherence to **WCAG AA** contrast standards across all UI surfaces ($\ge 4.5:1$ for body copy, $\ge 3:1$ for large text and interactive badges).
- Multi-modal status indication: Budget health must never be communicated through color alone; always pair color with text labels, icons, and exact currency amounts.
