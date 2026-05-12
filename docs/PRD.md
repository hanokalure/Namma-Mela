# Product Requirements Document (PRD)

## Namma-Mela — Theatre & Live Events Mobile App

| Document | Details |
|----------|---------|
| **Product** | Namma-Mela |
| **Platform** | Android (native) |
| **Version** | 1.0 (aligned with `versionName`) |
| **Last updated** | May 2026 |

---

## 1. Executive summary

**Namma-Mela** is an Android application that lets audiences discover plays and live performances, browse cast and genres, book seats, view digital tickets (including a scannable QR code), and interact through a community fan wall and reviews. Drama companies can use a **manager** experience to publish plays, configure seating, assign cast, and view booking/revenue insights. The product is designed as an **offline-first, on-device** experience: core data lives in a local database on the user’s phone; there is **no cloud backend** in the current implementation.

**Vision:** Bring the stage closer to fans—simple discovery, frictionless booking, and a sense of community around each show.

---

## 2. Goals

| Goal | Description |
|------|-------------|
| **G1 — Discovery** | Users can find active plays, search by title/genre/description, and browse curated categories and featured content. |
| **G2 — Booking** | Authenticated fans can select seats for an active play and receive a confirmation screen with booking details and entry QR. |
| **G3 — Trust & identity** | Users sign up and sign in locally; passwords are stored using strong hashing (PBKDF2); roles separate **fans** from **managers**. |
| **G4 — Operations** | Managers can maintain plays, tonight’s active show, hall seats, cast, and high-level revenue/booking metrics. |
| **G5 — Engagement** | Fans can post on a fan wall, react to posts, and submit written reviews linked to plays (subject to uniqueness rules in storage). |

**Success metrics (indicative):** install → registration → first booking funnel; manager-created plays with non-zero bookings; fan wall posts per weekly active user; review submission rate after attendance (tracked qualitatively until analytics exist).

---

## 3. Target users & personas

| Persona | Needs | Primary journeys |
|---------|--------|------------------|
| **Fan / attendee** | Discover shows, book seats, see ticket, optional community/reviews | Home → play detail → seat map → confirmation; Fan Wall; Profile |
| **Manager / drama company** | Publish plays, set active show, manage seats and cast, see insights | Admin login → dashboard → upload play / manage cast / seats / insights |
| **Casual browser** | Browse without booking | Home, search, cast discovery (limited without login for posting/reviews where required) |

---

## 4. Scope

### 4.1 In scope (current product)

- **Authentication:** Email-based registration and login; separate admin/manager login path; password hashing; session persistence via preferences.
- **Home:** Featured plays (carousel), category chips filtering active plays, shortcuts to search, notifications, profile; fan favorites strip with cast drill-down.
- **Search:** Debounced search across plays (title, genre, description).
- **Play detail:** Poster, metadata, cast; quick star rating vs. full written review flow; navigation to seat booking.
- **Seat booking:** Select seats for a play; persist bookings; navigate to ticket confirmation.
- **Ticket confirmation:** Show play, date/time, seats, price; **QR code** encoding booking/play identifiers for venue checks (validation logic at gate is out of app scope).
- **My bookings:** List user bookings; open ticket detail.
- **Notifications:** In-app list of notifications per user; mark read.
- **Fan Wall:** Chronological feed; post text and optional image (device picker); emoji shortcuts; reactions; delete own posts.
- **Cast:** Grid/browse cast with filters.
- **Profile:** Avatar, stats hooks, my bookings, logout; path to manager console for admin role.
- **Manager dashboard:** Tools for plays, cast, hall seats (when an active play exists), performance insights.
- **Insights:** Aggregated bookings/revenue/seat utilization vs. fixed hall capacity (in-app derived).
- **Data:** Room database; optional dev-only full reset in debug; default category seed where empty.

### 4.2 Out of scope (explicit)

- Cloud sync, multi-device accounts, and cross-phone booking recovery.
- Real payment gateways (UPI, cards, wallets); tickets are recorded as sold in-app only.
- Push notifications (FCM), email/SMS OTP, or password recovery flows.
- Server-side ticket fraud prevention or QR validation at the door (payload is generated in-app; venue systems would integrate separately).
- iOS or web clients.
- Accessibility audit / full WCAG compliance (recommended future work).

### 4.3 Future considerations

- Backend API, payments, and push notifications.
- Admin analytics export (CSV) and charts.
- Multi-language UI (Kannada/English, etc.).
- Instrumented tests and CI pipeline.

---

## 5. Functional requirements

### 5.1 Authentication & session

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-A1 | User can register with name, email, password; duplicate email rejected. | P0 |
| FR-A2 | User can log in; failed attempts show clear errors. | P0 |
| FR-A3 | Passwords stored hashed; legacy plaintext upgraded on successful login. | P0 |
| FR-A4 | Manager accounts use `ADMIN` role and dedicated login entry. | P0 |
| FR-A5 | Session restores on app launch unless logged out. | P1 |

### 5.2 Discovery & content

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-D1 | Home lists **active** plays only for primary listings; categories filter by genre/title/description overlap. | P0 |
| FR-D2 | Search returns matching plays in real time (debounced). | P0 |
| FR-D3 | Play detail shows booking CTA and paths to rating vs. full review. | P0 |

### 5.3 Booking & ticketing

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-B1 | User selects seats for a play and completes booking; booking stored with user, play, seats, price, show time context. | P0 |
| FR-B2 | Confirmation screen shows booking summary and QR encoding booking/play/timestamp payload. | P0 |

### 5.4 Social & reviews

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-S1 | Authenticated user can post on Fan Wall with text and optional image. | P1 |
| FR-S2 | User can react to posts; delete own posts from overflow menu. | P1 |
| FR-S3 | User can submit a written review for a play when signed in; conflicts handled via storage rules. | P1 |

### 5.5 Manager / admin

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-M1 | Manager can create/edit plays, set active play, manage cast and seats for a play. | P0 |
| FR-M2 | Manager dashboard surfaces booking counts and revenue for active play and insights screen. | P1 |
| FR-M3 | Manage seats requires an active play; user receives guidance if none set. | P1 |

---

## 6. Non-functional requirements

| ID | Category | Requirement |
|----|----------|-------------|
| NFR-1 | Performance | UI remains responsive; DB operations off main thread via coroutines/flows. |
| NFR-2 | Security | No plaintext password storage for new accounts; `local.properties` and keystores not committed to VCS. |
| NFR-3 | Reliability | Graceful empty/error states (e.g. no plays, failed loads). |
| NFR-4 | Maintainability | Modular layers (UI / ViewModel / repository / DAO); Hilt for DI. |
| NFR-5 | Compatibility | minSdk 24, targetSdk 34. |

---

## 7. Assumptions & constraints

- Single organization per manager profile is implied; multi-tenant isolation is not modeled in data.
- Seat capacity constants used for utilization are fixed in app logic unless made configurable later.
- Images use remote URLs or local URIs where applicable; large media policies are device-dependent.

---

## 8. Dependencies (engineering)

- Kotlin, Jetpack Compose, Material 3, Navigation Compose, Hilt, Room, DataStore, Glide (Compose), ZXing (QR), Coroutines/Flow, Gradle Android toolchain (see `app/build.gradle.kts`).

---

## 9. Release criteria (MVP gate)

- [ ] Fan can register, log in, browse active plays, complete a booking, see confirmation with QR.
- [ ] Manager can log in, set plays and active show, adjust seats/cast, view insights.
- [ ] Fan Wall post/reaction/delete-own works for signed-in user.
- [ ] Debug vs release behavior documented (e.g. optional dev DB reset only in debug).
- [ ] Release build produces installable artifact policy documented (signed release for store; debug APK for ad-hoc sharing).

---

## 10. Document ownership

- **Owner:** Product / engineering lead for Namma-Mela.
- **Review cadence:** Update when major features ship or backend scope changes.

---

*This PRD reflects the Android application as implemented in the repository; backend or Play Console policies may impose additional requirements when those are adopted.*
