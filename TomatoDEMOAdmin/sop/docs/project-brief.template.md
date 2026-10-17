# Project brief

Copy this file to `docs/project-brief.md` for each project. Replace bracketed fields with known facts; use `TBD` for unresolved decisions and `Not applicable` for excluded modules. A template entry is not an approval.

## Project identity

- Project and client: [Name]
- Owner and technical lead: [Names]
- Developer, reviewer, manual QA and release owner: [Names]
- Brief status and last update: [Draft or agreed; date]
- SOP baseline: Codemind Studio Developer SOP 1.0
- Existing product or new project: [Answer]
- Repositories and actual module paths: [List each app, API, admin and website]
- Approved scope or decision reference: [Task, issue, message or document]

## Purpose and scope

Business problem: [Who needs what and why]

Users and roles: [Customer, admin, staff, vendor or other roles with boundaries]

Included features: [Concrete list]

Excluded features: [Explicit exclusions]

Platforms: [Web, Android, iOS, desktop; required OS/browser support]

Mobile delivery: [Full Compose app, WebView wrapper, none; reason]

Offline, realtime, scale and accessibility requirements: [Only what is known]

## Stack and structure

For an existing project, replace defaults below with its actual stack. For new work, keep applicable defaults and mark unused modules as not applicable.

| Area | Selection | Version or source of truth |
| --- | --- | --- |
| Admin | Next.js and TypeScript | [Manifest and lockfile] |
| Website or web app | Next.js and TypeScript | [Manifest and lockfile] |
| API | Node.js and Express | [Runtime pin and manifest] |
| Backend language | TypeScript for new server or existing convention | [Decision] |
| Database | MongoDB and Mongoose | [Deployment and schema location] |
| Background jobs | Redis and BullMQ where required | [Jobs and worker process] |
| Media | Persistent uploads folder and relative paths | [Volume and media origin] |
| Full mobile app | Compose Multiplatform, Koin, Ktor, Coil | [Gradle catalog] |
| Navigation and state | Existing pattern or selected starter pattern | [Decision] |

Module layout and key entry points: [Actual paths]

Reference implementation: [Project and exact reusable feature; do not copy credentials]

## Product and integration decisions

- Design references, branding, fonts and locales: [Links and assets]
- Authentication and session expiration: [Provider, cookies/tokens, secure storage and logout]
- Permissions and tenancy: [Role-to-action and ownership rules]
- API contracts: [Location; routes, fields, errors, pagination, compatibility]
- Payments, notifications, maps and other providers: [Scope and account owner; no secrets]
- File types, size limits, visibility and retention: [Rules]
- Jobs, retries and idempotency: [Required operations and recovery]
- Data integrity and migrations: [Concurrency, indexes, transaction needs and rollback]
- WebView requirements if applicable: [Approved URL/hosts, external schemes, files, permissions, payment return]

## Environments and operations

Record dev, staging and production API origin, media origin, web origin and ports. List environment variable names and where secrets are managed, never actual secret values.

- Runtime and install commands: [Verified per module]
- Build and start commands: [Verified per module]
- Worker command and supervision: [If applicable]
- Persistent uploads and database backups: [Location, owner, retention, restore procedure]
- Deployment target, logs, monitoring and rollback: [Details]
- Deployment authorization and responsible person: [Reference or pending]

## Acceptance and verification

Verification mode: [Owner manual testing with build checks only, or agreed automated checks plus manual QA]

Automated tests allowed: [Yes/no; scope]

Build/compile checks allowed: [Exact commands and module directories; confirm they do not invoke forbidden suites]

| Feature | Acceptance criterion | Verifier and evidence |
| --- | --- | --- |
| [Feature] | Given [state], when [action], then [observable result] | [Owner; screenshot, response or check] |
| [Failure case] | Given [failure], the user sees [recovery behaviour] | [Owner and result] |

Manual scenarios: [Include relevant auth, roles, validation, uploads, payment states and each required platform]

Unverified areas and release blockers: [Facts]

## Approved exceptions

| Default | Project decision and reason | Scope | Approval reference and date |
| --- | --- | --- | --- |
| [SOP default] | [Replacement and consequence] | [Modules] | [Actual approval or pending] |

Other SOP rules continue to apply. Existing stack facts can be recorded separately without pretending that a fresh approval occurred.

## Open questions and change history

Open questions: [Only unresolved consequential questions, owner and dependent work]

Change history: [Date, decision or scope change, reference and impact]
