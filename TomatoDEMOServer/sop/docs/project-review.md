# Project review supporting the SOP

Reviewed on 8 October 2026. These findings describe accessible local snapshots. They do not establish that a project is completed, currently deployed, secure, or functionally passing. The review read package and Gradle manifests, existing instructions, directory structure and selected implementation paths. No project application code was changed and no application builds, tests or production services were run for this documentation task.

## Reference roots

| Reference | Local root inspected |
| --- | --- |
| Mrigmaya | `/Users/devsharma/Desktop/Mrigmaya` |
| Kroove | `/Users/devsharma/Desktop/Kroove` |
| XMWebsite | `/Users/devsharma/Desktop/XMWebsite` |
| Azhame web and API | `/Users/devsharma/Desktop/AzhameData` |
| Azhame mobile wrapper | `/Users/devsharma/AndroidStudioProjects/AzhameWebview` |
| TractorWala web and API | `/Users/devsharma/Desktop/TractorWalaMarketPlace` |
| TractorWala wrapper | `/Users/devsharma/AndroidStudioProjects/TractorWalaMarketPlace` |
| BiteBoxx web and API | `/Users/devsharma/Desktop/BiteBoxx` |
| BiteBoxx app snapshot | `/Users/devsharma/Desktop/BiteBoxx/BiteBoxxApp` |
| Froolt web and API | `/Users/devsharma/Desktop/Froolt` |
| Additional Froolt snapshot | `/Users/devsharma/Desktop/Froolt-API-Server` |
| Froolt mobile | `/Users/devsharma/AndroidStudioProjects/Froolt` |
| Aaj Ki Pehal mobile | `/Users/devsharma/AndroidStudioProjects/AajKiPehalNews` |
| Lykechat mobile conventions | `/Users/devsharma/AndroidStudioProjects/Lykechat-Android-IOS` |

The earlier path `Desktop/FROOLT BUSSINESS` was absent. The two accessible Froolt server snapshots above were used; neither was assumed to be the authoritative production checkout. BiteBoxx's app instructions reference a different AndroidStudioProjects location; this review used the app snapshot actually found under Desktop/BiteBoxx.

## Findings by project

Paths below are relative to the corresponding root above.

### Mrigmaya

`Website/package.json` declares Next.js and TypeScript. `Admin/package.json` declares React, Vite, TypeScript and TanStack Query. `Server/package.json` declares Express, Mongoose, TypeScript, Zod, Multer and Sharp. `Server/src/worker.ts` and `Server/src/jobs.ts` show database-backed job processing, including state and retry handling. This supports the website and API conventions; it does not establish BullMQ usage. The admin is an existing Vite exception to the owner's new Next.js default.

### Kroove

`Website/package.json` declares Next.js; `Admin/package.json` declares Vite/React; `Server/package.json` uses TypeScript ES modules with Express and Mongoose. `Website/src/lib/api.ts` centralizes API calls, cookie credentials, CSRF handling and media paths. `Server/src/worker.ts` invokes a custom jobs loop. Reuse the API/media boundary, but retain the project's response shape and existing jobs implementation until a deliberate migration.

### XMWebsite

`Website/package.json` declares React, TanStack Start/Router, Vite and Nitro, rather than Next.js. It defines project-specific port 8093 and separate build/start scripts. `Server/package.json` declares Express, Mongoose, TypeScript, Zod and WebSocket support, with a targeted pending-orders test script. This is a manifest-level stack and command reference, not a review of trading logic or financial correctness. Its port, framework and realtime dependencies must not become universal company defaults.

### Azhame

The web and admin manifests under `AzhameData` declare Vite/React. `azhame-server/package.json` declares Express, Mongoose and Multer with JavaScript ES modules. The root instructions describe controller/service boundaries. In the wrapper, `shared/src/commonMain/kotlin/com/azhame/application/WebView.kt` declares an expected WebView; Android and iOS implementation files contain platform navigation and external app handling. Their relevant sections were inspected for payment link, popup, upload and return considerations, without a device run.

### TractorWala

`Admin/package.json` and `Website/package.json` provide a direct Next.js admin/website reference. `Admin/lib/api.ts` uses a shared Axios client at `/api/v1` with cookie credentials and a timeout. `Server/package.json` declares Express/Mongoose, Zod, Multer and Sharp; `Server/src/app.js` serves the uploads directory. The mobile wrapper has shared and platform WebView files. Several manifest entries use `latest`; those entries are not adopted as the version policy for new work.

### BiteBoxx

`Server/package.json` and representative source files show a CommonJS Express/Mongoose/Zod server. The admin and operational panels use Vite/React. `Server/src/services/upload.service.js` returns a relative catalog upload path, and `src/config/uploadPaths.js` contains path containment and legacy upload resolution. `src/middleware/errorHandler.js` and Froolt's response helper informed the consistency rule; this SOP adds explicit safe-error requirements rather than endorsing every existing error response.

In the app snapshot, `gradle/libs.versions.toml`, `shared/src/commonMain/kotlin/com/biteboxx/application/di/AppModule.kt` and `network/ApiClient.kt` show Koin-managed repositories and a reusable Ktor client. Platform HTTP client files and feature repositories support the shared/platform separation. The catalogue lists Coil, Voyager, coroutines, serialization, datetime, settings and Napier. Presence in the catalogue alone is not proof every library is used in every feature.

### Froolt

`FrooltServer/package.json` declares CommonJS, Express, Mongoose, Zod, BullMQ and ioredis. `src/queues/whatsappCampaign.queue.js` constructs a BullMQ queue with stable campaign job IDs. `src/workers/campaignWorkerLauncher.js` launches a worker process. `src/controllers/admin/category.controller.js`, `src/services/admin/category.service.js` and `src/utils/apiResponse.js` show controller/service/model separation and the `isSuccess`, `message`, `data` envelope. `src/app.js` exposes uploads.

The inspected queue removes completed jobs. The SOP therefore requires durable business idempotency in addition to stable job IDs; it does not claim those IDs guarantee one execution. Worker supervision and recovery in the SOP are operating standards, not a certification of this launcher.

`FrooltServer/package.json` contains placeholder lint and test scripts. The additional `Froolt-API-Server/server/package.json` also has placeholder scripts. Existing AGENTS instructions refer to `server/` and `admin-panel/`, while the main `Desktop/Froolt` snapshot has named folders such as `FrooltServer` and `FrooltAdminPanel`. This directly supports verifying code and scripts instead of trusting documentation blindly.

The mobile Gradle catalogue and source inventory show the Compose app layout, Koin/Ktor/Coil, Voyager and optional integrations. Versions and feature-specific libraries are not copied wholesale into the SOP.

### Additional mobile references

Aaj Ki Pehal's `AGENTS.md`, `docs/ARCHITECTURE.md`, Gradle catalogue and the common/Android/iOS `core/network/HttpClientFactory.kt` files were inspected. They explicitly separate common contracts from OkHttp/Darwin implementations and describe lifecycle-owned Koin, cancellation, an image client without API credentials, shared theme/resources and manual-owner verification. It is a useful recent architecture scaffold, not evidence of a finished feature-rich application.

Lykechat's `AGENTS.md` and source inventory provided a secondary reference for the `composeApp` layout, shared DI/networking and native boundaries. Its business features were not audited.

## How findings become standards

| Basis | SOP decision |
| --- | --- |
| Explicit owner requirement | New admin Next.js; Express/MongoDB; Redis/BullMQ; uploads folder and saved path; Compose/Koin/Ktor/Coil; WebView wrappers; separate brief and agent instructions |
| Repeated code patterns | Layered API; shared client/media helpers; Mongoose; Zod; common/platform source sets; Gradle version catalogues |
| Selected new-project defaults | Next.js customer website, TypeScript ES-module backend, Voyager where compatible |
| Engineering requirements added by this SOP | Durable job idempotency, bounded retries, persistent uploads, safe error responses, secure credential storage, explicit verification evidence |
| Existing differences retained | Vite/TanStack web stacks, CommonJS servers, custom database workers and project-specific modules |

The new requirements are not claims that all reviewed projects already satisfy them. No company-wide latest-version list, hosting provider, test framework, payment provider or automatic migration is imposed.

## Official references checked

- [Codex AGENTS.md discovery](https://learn.chatgpt.com/docs/agent-configuration/agents-md): placement and directory overrides.
- [Kotlin expected and actual declarations](https://kotlinlang.org/docs/multiplatform/multiplatform-expect-actual.html): platform implementation boundaries.
- [BullMQ idempotent jobs](https://docs.bullmq.io/patterns/idempotent-jobs): retry-safe business outcomes.
- [Next.js Server and Client Components](https://nextjs.org/docs/app/getting-started/server-and-client-components): server/client responsibilities.
