# GiveMatch

### Smart Item Donation & Distribution System

A JavaFX desktop application that turns informal, word-of-mouth item donation into a structured platform — where donors list what they can give, receivers request what they need, and the system fairly handles the cases where supply can't cover demand.

Built for the Design Patterns Lab final project.

---

## 1. Overview

Donating usable items — books, clothes, school supplies, household goods — usually happens through scattered social media posts or personal networks. That works, but it breaks down quickly: donors have no idea who actually needs what they're giving away, receivers have no central place to check what's available, and when several people want the same limited item, there's no fair or transparent way to decide who gets it. Quantities go unrecorded, and nothing keeps a history of what was donated, requested, or actually delivered.

GiveMatch replaces that informal process with a single application: donors post items, receivers search and request them, and the platform itself enforces availability limits, resolves competing requests through a defined policy, and keeps a full audit trail from donation to delivery.

---

## 2. Key Features

- **Role-based accounts** — Donor, Receiver, and Admin, with hashed authentication
- **Donation listings** — post an item under a category with quantity and description
- **Search & request** — browse available items by category and request a specific quantity
- **Standing needs** — receivers can register a recurring need and get notified automatically instead of checking back manually
- **Fair allocation on contested items** — when total requests exceed what's available, a configurable policy (not first-come luck) decides the outcome
- **Full lifecycle tracking** — every donation and every request has a defined status, so nothing is left ambiguous
- **Distribution log** — every completed handoff is recorded, tied back to the original request
- **Analytics dashboard** — most in-demand unmet categories, fulfillment rate over time

---

## 3. Tech Stack

| Layer | Technology |
|---|---|
| Desktop UI | JavaFX |
| Build / Dependencies | Maven |
| Persistence | SQLite via JDBC |

---

## 4. Data Model

| Entity | Purpose | Key Fields |
|---|---|---|
| `Users` | Donor / Receiver / Admin accounts | id, name, role, hashed_password, contact_info |
| `Categories` | Item classification | id, name, description |
| `Donations` | Items posted by donors | id, donor_id, category_id, quantity, quantity_remaining, status |
| `NeedRequests` | Optional standing needs | id, receiver_id, category_id, quantity_needed, status |
| `Requests` | A receiver's request against a specific donation | id, donation_id, receiver_id, quantity_requested, status |
| `Distributions` | Record of a completed handoff | id, request_id, quantity_distributed, distributed_at |

All relationships are enforced with primary/foreign keys and constraints. Seeder scripts populate default categories and sample data on first run.

---

## 5. Core Workflows

**A. Direct Request**

Search by category → View available item → Request quantity
→ Availability check → Accepted (Pending) or Rejected
→ If contested: allocation policy resolves competing requests
→ Donor approves → Distribution recorded → Quantity updated

**B. Standing Need Match**

Receiver posts a standing need → System monitors new donations
→ Notification on category match → Proceeds via Direct Request flow

---

## 6. Design Patterns

Each pattern below was chosen to solve a specific structural problem in the app — not added to satisfy a quota.

### Strategy — Allocation Policy
**Problem:** When a donation is contested by multiple receivers, someone has to decide who gets priority — and that logic shouldn't be hardcoded into the request handler.
**Solution:** `AllocationStrategy` interface with `FIFOAllocation`, `PriorityBasedAllocation`, and `ProportionalAllocation` implementations, selected at runtime.
**Benefit:** A new allocation rule can be added later without touching existing request-processing code.

### State — Donation & Request Lifecycles
**Problem:** Both donations and requests move through multiple statuses, and not every transition is valid from every state (e.g. a completed request can't go back to pending).
**Solution:** Separate `DonationState` and `RequestState` hierarchies, each enforcing only its own legal transitions.
**Benefit:** Invalid status changes are structurally impossible, and status-dependent behavior isn't scattered across conditional checks throughout the codebase.

### Observer — Standing Need Notifications
**Problem:** When a new donation matches an existing standing need, one or more parts of the system need to react — without the donation-posting logic knowing who's listening.
**Solution:** Donations act as a subject; notification channels (in-app list, audit log) subscribe as observers.
**Benefit:** New notification channels can be added later without modifying how donations are posted.

---

## 7. Reporting

- Unmet needs by category
- Fulfillment rate by donor, category, or time period

---

## 8. Setup and Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.8+
- macOS (default), Linux, or Windows (override with `-Djavafx.platform`)

### Build
```bash
mvn clean compile
```

### Run
```bash
mvn clean compile exec:java
```

Or to build an executable JAR:
```bash
mvn clean package
java -jar target/givematch.jar
```

### First Launch
- The database (`givematch.db`) is created automatically on first run in the project directory
- Default admin account:
  - **Username:** `admin`
  - **Password:** `admin123`
- Sample categories and donations are seeded automatically

---

## 9. Database

The application uses SQLite with automatic schema initialization. The database file (`givematch.db`) is created in the working directory on first launch.

**Schema includes:**
- `users` — All registered users (Donor, Receiver, Admin)
- `categories` — Item categories (auto-seeded with 8 defaults)
- `donations` — Items posted by donors
- `need_requests` — Standing needs registered by receivers
- `requests` — Specific requests for donations
- `distributions` — Record of completed handoffs

All relationships are enforced with foreign keys and constraints.

---

## 10. User Workflows

### Administrator
1. Log in with `admin` / `admin123`
2. Manage categories, view analytics, and set allocation strategies
3. Lookup users and adjust priority levels

### Donor
1. Register or log in
2. Post donations with category, quantity, and description
3. Review incoming requests
4. Approve requests and complete distributions

### Receiver
1. Register or log in
2. Browse available items by category
3. Create requests for specific donations
4. Register standing needs for automatic notifications

---

## 8. Team

| Name | Roll |
|---|---|
| Jannatul Ferdous | 1646 |
| Nabila Siddiqua| 1644 |
