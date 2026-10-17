# Codemind Studio Developer SOP

Version 1.0 • 8 October 2026 • For developers, technical leads and coding agents

We use this SOP to select our default stack, organise code, implement features and hand work over consistently. A fresher should be able to start a supervised task with it; an experienced developer or agent should be able to make routine decisions without repeatedly asking the owner.

Use this SOP with the separate project brief and the existing code. Client requirements can change a specific default. Record that decision in the brief and continue following the remaining applicable standards. This document defines the working baseline; it does not authorise migration of existing projects.

## 1 Start a project or task

### Read before implementing

Read the applicable `AGENTS.md`, `docs/project-brief.md`, this SOP, the README, dependency manifests and the relevant code. Check the working tree and any companion repositories before editing. The project brief owns client scope, platforms, integration choices, environment details and approved exceptions. Keep passwords and actual secret values out of all these documents.

For an existing project, verify what the code actually does. An old README can describe a different directory layout or claim a test command that is only a placeholder. Preserve unrelated changes and the established architecture unless the current task authorises changing it.

### Ask only consequential unanswered questions

Establish the product type, users and roles, required platforms, core flows, integrations, offline needs, design references and acceptance criteria. Distinguish a full mobile product from a wrapper for an existing web app. Ask about unresolved matters that change scope, data integrity, cost or architecture. Do not ask again for information already in the brief or an approved conversation.

If the brief is missing, create a short draft from known requirements, label assumptions and ask only blocking questions. Continue independent work while a decision is pending. Never invent client approval, payment credentials, production endpoints or completed integrations.

### Record decisions and proceed

Provide a short implementation approach covering affected modules, selected stack, API changes and verification. Routine choices within agreed scope do not require another approval. A new project can proceed on these defaults when the requested scope is clear. Resolve any remaining major product decision with the owner before implementing the dependent part.

An exception record includes the default, replacement, reason, affected modules, approval reference, date and maintenance consequences. Approval already given in the task counts; record it rather than asking again. Existing architecture is a compatibility constraint, not a reason to request a migration.

Example: a client explicitly requires PostgreSQL. Record that database exception and the compatible data access approach. The admin can still use Next.js, the API Express, and the mobile app Compose Multiplatform.

## 2 Select the stack

The owner specified Next.js for new admin panels; Express, MongoDB, Redis and BullMQ for the backend stack; local upload paths for files; and Compose Multiplatform with Koin, Ktor and Coil for full mobile apps. The website and supporting-library defaults below are selected from reviewed project patterns.

| Product or layer | Default for new work | Application rule |
| --- | --- | --- |
| Admin and operational panels | Next.js, React, TypeScript | App Router; Express remains the business API |
| Website or web app | Next.js, React, TypeScript | Default derived from TractorWala, Kroove and Mrigmaya; retain existing Vite or TanStack applications |
| API | Node.js, Express, MongoDB with Mongoose | REST; retain a project's JavaScript or TypeScript convention |
| Background processing | Redis, BullMQ, ioredis | Use for real queued or delayed work; do not provision an unused worker for a static site |
| Files and images | Server `uploads/` directory | Save relative paths in MongoDB; resolve through configured media origin |
| Full Android and iOS app | Kotlin, Compose Multiplatform, Material 3 | Shared logic and UI with platform implementations |
| Mobile dependencies | Koin, Ktor, Coil | Constructor injection, shared API client and image loading |
| Existing web app packaged for mobile | Compose host with platform WebViews | Load the approved URL; Android WebView and iOS WKWebView |

For a new backend, prefer TypeScript with ES modules as a maintainability choice based on the newer commerce projects. Existing CommonJS JavaScript servers, including BiteBoxx and Froolt, remain valid. Do not convert module systems as incidental cleanup.

Use Zod for new Express request validation, following the reviewed server patterns. Use the existing web API wrapper; choose one fetch or Axios wrapper when starting fresh. TanStack Query is suitable when client-side server-state caching is needed. Preserve an existing form and UI library. Add React Hook Form, charts, animations or other packages only for actual features.

Mobile supporting libraries observed across the references include kotlinx coroutines, serialization and datetime, Voyager, multiplatform settings and Napier. Voyager is the starting navigation preference where compatible; preference storage is for non-sensitive settings. Payments, maps, Firebase, media playback and offline databases are feature-specific, not mandatory starter dependencies.

Pin the chosen runtime and dependency baseline in project manifests and lockfiles. Check compatibility before adopting versions; do not copy `latest`, alpha or beta versions from a reference merely because they exist there. Keep Gradle versions in `gradle/libs.versions.toml`. A reference project's version is evidence of that project, not a company-wide upgrade target.

## 3 Organise the code

Use clear responsibility boundaries. For new projects, the following layout is a starting convention; create only modules required by the scope. Existing directory names and repository boundaries take precedence over cosmetic consistency.

```text
project/
  AGENTS.md
  docs/developer-sop.md
  docs/project-brief.md
  Admin/                 Next.js panel when required
  Website/               Customer web product when required
  Server/                Express API and worker code
  Mobile/                Compose project when required
```

Separate repositories are also valid. Record their actual paths or repository URLs in the brief. Never assume the mobile checkout contains its companion API. Each service owns its dependencies, configuration and deployment instructions.

### Backend boundaries

Use `src/config`, `routes`, `middleware`, `validators`, `controllers`, `services`, `models`, `utils`, and, when needed, `queues` and `workers`. Group larger features within those folders or use the project's established feature layout.

The request flow is route → authentication and validation → controller → service → model or integration. Controllers translate HTTP input and output. Services own business rules. Models own database schemas and indexes. Queue producers enqueue work; worker handlers call services. Keep bootstrapping separate from route implementation.

### Web boundaries

Use `app` for Next.js routes and layouts, `components` for shared UI, `features` for feature-specific code, and `lib` for API, media and configuration helpers. Define shared types and theme tokens centrally. Avoid duplicating API calls, session handling or formatting across screens. Do not import MongoDB models or private server configuration into browser code.

### Mobile boundaries

Use either the existing `shared` module with thin `androidApp` and `iosApp` hosts, or the existing `composeApp` layout. New shared features can use `feature/<name>/data`, `domain` and `presentation`, with shared infrastructure in `core`. Add layers when they contain real behaviour; avoid empty repositories and speculative abstractions.

Place common Kotlin in `commonMain`, native implementations in `androidMain` and `iosMain`, and shared assets in `composeResources`. Use PascalCase for classes and components, camelCase for functions and variables, and descriptive filenames. Follow the repository formatter and naming conventions. Favour focused changes over unrelated restructuring.

## 4 Build APIs and protect data

Validate body, path and query values at the API boundary. Allowlist writable fields, filter keys and sort options. Bound page sizes and search inputs. Do not pass raw request objects into MongoDB queries or updates. Enforce roles, tenant boundaries and record ownership on the server for every protected action.

Keep the existing response contract. For new APIs, the following envelope follows the Froolt pattern; document field meanings and pagination in the project contract.

```json
{
  "isSuccess": true,
  "message": "Categories fetched",
  "data": [],
  "meta": { "page": 1, "limit": 20, "total": 0 }
}
```

Use meaningful HTTP status codes and a consistent safe error shape. Return field errors where users can correct input; log internal failures with a correlation ID. Do not expose stack traces, credentials or raw provider errors. An empty collection can be a successful response; a network or database failure must not silently become an empty collection.

For MongoDB, define required fields, timestamps and indexes that match actual queries. Apply deterministic sorting and pagination. Use uniqueness constraints for identities and idempotency records. Store monetary amounts as integer minor units with currency, or an explicitly chosen exact decimal representation. Store instants consistently in UTC and apply the configured timezone only when presenting them or evaluating local schedules.

Inventory, payments and refunds need controlled state transitions and concurrency protection. Use atomic updates or transactions when related writes must succeed together; confirm the deployment supports the selected transaction approach. Never trust client-supplied totals, roles or payment success. Verify provider callbacks and process duplicate events safely. Keep a reconciliation path for uncertain payment or shipment outcomes.

Choose session handling in the brief. For new browser sessions, prefer appropriately configured HttpOnly cookies, CSRF protection and explicit CORS origins. Mobile token storage must use a platform-secure abstraction where credentials are retained. Session expiration clears private cached state and returns the user to authentication without a retry loop. Hiding an admin button never replaces server authorization.

Keep third-party integrations behind service boundaries with bounded timeouts and explicit error mapping. Do not automatically retry a mutation that can create duplicate charges, orders or external resources. Document the compatibility impact before changing a response used by multiple clients.

## 5 Handle images and uploads

The standard flow is: authenticated upload → validation → file saved under server `uploads/` → relative path stored in MongoDB → web or mobile media helper resolves the display URL.

Example: the physical file is `Server/uploads/catalog/item-unique.webp`; the database stores `/uploads/catalog/item-unique.webp`. A configured media origin such as `https://media.example.com` produces `https://media.example.com/uploads/catalog/item-unique.webp`. These are illustrative values. Do not store a developer's absolute disk path or a hardcoded development origin in production records.

### Accept and serve files deliberately

For new endpoints, prefer multipart uploads and a maintained compatible upload parser. Preserve a legacy base64 contract until a coordinated change is authorised. Enforce body limits before decoding; validate allowed file types, actual decoded content, file size and image dimensions. Generate safe unique names and prevent path traversal or symlink escape.

Use image processing such as Sharp when resizing, compression or thumbnails are required. Save metadata and references only after the file operation succeeds; handle cleanup when a later database write fails. Define whether replacing an image deletes an unreferenced previous file and when cleanup runs. Do not delete a file still used elsewhere.

Public product images can be served under a public uploads route. Private documents require an authorized retrieval path and must not be included in a public static directory. Upload permission and file-read permission are separate decisions.

### Resolve images consistently

Create one media URL helper per client. It should handle missing images and only supported path formats. If a legacy API returns absolute URLs, support them deliberately with an allowed-origin policy. Avoid prepending the media origin twice or confusing `/api/v1` with the media root. Use placeholders, fixed image bounds and appropriate loading or failure states; use Coil for shared mobile image loading.

### Keep files across deployments

The uploads directory is persistent application data. Mount a persistent volume or equivalent durable filesystem, document its location, and back it up with a tested restore procedure. A rebuild, container replacement or release cleanup must not erase uploads. Multiple API or worker instances need coordinated access to the same files. If the hosting platform cannot support this, record an approved storage exception and update the media contract before deployment.

## 6 Run background work reliably

Use Redis and BullMQ for approved queued work such as notifications, exports, image or video processing, scheduled actions and reconciliation. MongoDB remains the source of business records. A Redis cache has explicit TTLs and invalidation; cache keys must include the scope needed to prevent cross-user or cross-tenant leakage.

The API producer records or validates the operation and enqueues a small payload containing stable record identifiers. A worker loads the current record, checks whether the action is still valid, executes the service and records its outcome. Avoid putting secrets, large binary files or stale full records in jobs.

Define queue name, worker owner, timeout, concurrency, retry/backoff policy, retention and terminal failure handling. Retry transient failures within bounds. Keep jobs small enough to resume and diagnose. A retry must leave the business result consistent; this is idempotency. See the [BullMQ idempotent job guidance](https://docs.bullmq.io/patterns/idempotent-jobs).

Use a stable job identifier to reduce duplicate scheduling, plus database-level state or idempotency protection for side effects. Job IDs alone do not guarantee one-time execution, especially after completed jobs are removed. Verify payment or provider outcomes before retrying an uncertain mutation.

Where a database write and queue submission must stay consistent, use a durable outbox or a documented reconciliation process. If Redis is unavailable, record pending work durably or return an explicit failure according to the contract. Do not claim that a notification or export has completed just because enqueueing was attempted.

Run production workers as separately supervised processes where practical. Deploy them with the matching application code and configuration. Monitor failed and stalled jobs, queue age and worker health. Handle shutdown by stopping intake and completing or safely releasing active work. Protect queue administration and retry operations.

Froolt provides an actual BullMQ reference. Kroove and Mrigmaya use custom database-backed job loops in the inspected snapshots. Keep those existing implementations unless migration is explicitly in scope; use Redis/BullMQ as the new-project default. Neither the presence of a `worker` script nor a dependency entry proves a worker is running in production.

## 7 Build web and admin interfaces

Use Next.js and TypeScript for new admin panels. The Express API owns shared business rules and data access. Next.js can handle presentation and purposeful server-side forwarding, but do not create a second competing business API inside route handlers.

Use Server Components for suitable server-rendered content and Client Components where interaction, state or browser APIs are required. Place the client boundary deliberately. Keep secrets on the server; public environment variables are shipped to the browser. Follow the installed Next.js version and its [Server and Client Components guidance](https://nextjs.org/docs/app/getting-started/server-and-client-components).

Centralize request timeouts, authentication, error handling, media URLs and logout behaviour. Use typed request and response data. When using a query cache, invalidate affected queries after mutations and scope or clear private caches when the user or tenant changes. Avoid caching private responses as shared public content.

Every data screen needs loading, empty, success, failure and retry behaviour where appropriate. Forms require labels, useful validation messages, submit progress and duplicate-submit protection. Destructive actions must make the consequence clear. Lists need appropriate search, filtering, pagination and stable row identity. When users select a location on a map, include text search and allow them to adjust the final pin.

Reuse accessible components and shared colour, type, spacing and shape tokens. Keep layouts usable on the target desktop and mobile sizes, with keyboard navigation, visible focus, readable contrast and sufficient touch targets. Preserve the project's logo, fonts and visual direction. Add localised strings and date or number formatting where required by the brief.

For public websites, implement meaningful page titles, metadata and accessible content; choose rendering and caching according to freshness and SEO needs. Optimise image sizes, reserve media dimensions and avoid loading large feature bundles before they are needed. Do not add charts, animation or visual libraries without a user-facing purpose.

Current Vite panels remain Vite. XMWebsite's TanStack Start setup remains its own established stack. Ports, domains, map providers and payment gateways are project configuration, never universal values copied from another client. Verify API and media connectivity for the selected environment before delivery.

## 8 Build full mobile applications

Compose Multiplatform is the default for shared Android and iOS UI and logic. Keep hosts thin and implement platform SDK work in platform source sets. Use `expect` in common code and matching `actual` implementations at real platform boundaries; ordinary shared business logic does not need duplicate platform implementations. See [Kotlin expected and actual declarations](https://kotlinlang.org/docs/multiplatform/multiplatform-expect-actual.html).

A verified boundary in Aaj Ki Pehal is `platformHttpClient(config)`: Android uses the Ktor OkHttp engine and iOS uses Darwin. Follow that separation for platform capabilities such as secure storage, permissions, location, payments, sharing and external URL launching. Supply implementations for every required target and preserve coroutine cancellation when mapping failures.

Use Koin with constructor injection. Start and own the dependency container once according to the app's existing lifecycle. Reuse HTTP clients and image loaders; do not create them inside composables or per request. Scope ViewModels and state holders to their proper lifecycle. Avoid global mutable service state and `GlobalScope` for feature work.

Keep API DTOs in the data layer and map them to models used by the feature. Repositories own data access; ViewModels or existing state holders expose immutable UI state. Composables display state and emit user actions. Distinguish loading, content, empty and error states and preserve user input across supported lifecycle events.

Ktor owns networking and serialization; Coil owns remote image loading. Keep image requests separate from authenticated API defaults so bearer tokens are never forwarded to arbitrary image hosts. Apply timeouts and safe retry semantics. Use kotlinx serialization, coroutines and datetime where needed; use multiplatform settings only for non-sensitive preferences. Introduce a secure credential store when authentication requires persistence.

Follow existing navigation, including Voyager where already used. Pass stable IDs through routes, not clients or large mutable objects. Clear protected navigation and session data when login expires. Keep theme colours, typography, dimensions and shared strings in reusable resources. Respect font scaling, insets, accessibility labels and both platform behaviours.

Add payments, push, maps, camera, media playback or offline storage only when the brief requires them. Never copy another client's application ID, signing key, Firebase configuration, URL or store metadata. An Android compilation does not verify iOS. A Kotlin iOS framework compilation does not verify the Xcode host or runtime behaviour.

## 9 Package an existing web app for mobile

Use a WebView wrapper when the agreed product is the existing website delivered inside Android and iOS apps. A full mobile implementation remains a different scope. AzhameWebview and TractorWalaMarketPlace are the inspected wrapper references; their shared `WebView` contract has separate Android and iOS implementations.

Record the approved base URL, internal host allowlist, required external schemes and platform features in the brief. Load production content over HTTPS. Keep authentication sessions consistent with the website and define logout, back navigation, popup handling, refresh, loading, offline and retry behaviour.

Handle external app links deliberately. Payment or UPI schemes must be dispatched to the platform with safe fallback when the target app is unavailable. Returning from a payment app must trigger the appropriate server-backed status refresh; a successful app launch or return is not payment confirmation. Validate navigation targets and any intent fallback before opening them.

Avoid blanket handling that sends every unknown URL to an external application. Keep privileged JavaScript bridges minimal, origin-restricted and limited to required features. Do not bypass SSL errors or enable unrestricted local file access to make a broken flow appear to work. Grant camera, location, microphone or file access only for the actual feature and approved origins.

Implement file chooser, download, keyboard, safe-area and rotation behaviour where required. Test popup checkout, authentication redirects and back-stack recovery on each platform. Platform-specific configuration may differ; do not assume Android link dispatch or permissions automatically apply to iOS.

The manual handover checklist covers login and logout, session expiry, navigation, offline recovery, file upload, external app installed and absent, payment success, cancellation and failure, and return from the background. Mark unsupported features explicitly. Verify current store and provider requirements before submission rather than assuming any wrapper will be accepted.

## 10 Verify and deliver the work

### Choose and report verification

Follow the task and project verification policy. If the owner explicitly says manual testing only, run only the allowed build or compile checks and provide manual checks for the owner. Do not add automated test tooling, run suites or bypass that preference indirectly. Otherwise, run existing relevant checks proportionate to the change and add meaningful regression coverage for material behavioural risk.

Read scripts before running them. A JavaScript server may have no build command. Froolt's inspected test and lint scripts are placeholders; a zero exit code from them is not validation. Use documented non-mutating syntax or compilation checks when appropriate and report their limits. Do not seed a database or start a worker against a live environment as a routine build check.

Record command, module, result and limitation. For mobile, distinguish Android build, shared iOS compile, Xcode host build and device verification. For UI and integrations, provide concise manual steps and expected outcomes. Build success alone never means all functional checks passed.

### Definition of done

- The agreed behaviour and acceptance criteria are implemented with appropriate loading and failure states.
- API consumers, permissions, data integrity, uploads and platform boundaries affected by the change are accounted for.
- Relevant allowed checks have passed, or blockers and unverified areas are stated plainly.
- Configuration examples, contracts and the project brief reflect material changes without exposing secrets.
- The diff is focused; unrelated changes are preserved. Review notes explain the result and material risks.

### Release and ownership

Before an authorised release, confirm environment configuration, private credentials, persistent uploads, database compatibility, worker startup, monitoring, backups and rollback steps. Verify readiness without exposing sensitive diagnostics. Smoke-check the key flows after deployment and record the release identifier and owner. A request to implement does not by itself mean publish to production, spend money or send client messages.

The developer implements and reports evidence. The reviewer checks behaviour, boundaries and risks. The designated QA owner completes manual acceptance. The project owner resolves scope and exceptions; the release owner performs the authorised deployment. Assign actual people in the project brief; one person can hold multiple roles.

For onboarding, give the new developer this SOP, the brief and a small supervised feature. Ask them to explain the request path, platform boundaries and verification result before handing over larger work. Update the SOP when a reusable standard changes, version it, and deliberately roll it into active projects. Do not silently alter historical project decisions.

## Reference basis

The accompanying `project-review.md` records inspected local snapshots, source paths and differences. The review covered manifests, instructions and representative implementation paths; it was not a runtime, production, security or completion audit. The main references were Mrigmaya, Kroove, XMWebsite, Azhame, TractorWala, BiteBoxx and Froolt, with Aaj Ki Pehal and Lykechat used for additional mobile conventions.

Keep the full SOP in `docs/developer-sop.md` and the concise agent entry point at repository-root `AGENTS.md`. The agent entry point explicitly tells the agent to read the separate brief and SOP. Codex supports directory-scoped instructions; verify the active instruction sources when installing the pack. See [official AGENTS.md guidance](https://learn.chatgpt.com/docs/agent-configuration/agents-md).
