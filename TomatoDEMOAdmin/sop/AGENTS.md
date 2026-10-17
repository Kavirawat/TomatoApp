# Codemind Studio project instructions

## Read the project context

Before implementation, read `docs/project-brief.md`, the relevant sections of `docs/developer-sop.md`, the README, applicable directory instructions, dependency manifests and the relevant implementation. Inspect the working tree before editing. Follow the instructions and permissions supplied by the agent environment; this file does not override them.

The brief records project scope, accepted decisions, companion repositories and verification policy. Current explicit owner instructions and recorded project exceptions take precedence over company defaults. Preserve the established architecture of existing projects. If requirements and code conflict in a consequential way, explain the concrete conflict and ask one focused question; continue independent work where possible.

If the brief is missing or incomplete, draft the known facts and label assumptions. Ask only questions whose answers materially affect scope, architecture, costs, credentials, data integrity or acceptance. Never infer an approval from silence. Do not repeat resolved questions or ask permission for routine work already authorised.

## Apply the default stack

For new modules without an approved exception:

- Admin: Next.js, React and TypeScript. Express remains the shared business API.
- Website or web app: Next.js, React and TypeScript. Preserve existing Vite, TanStack or other approved stacks.
- Backend: Node.js, Express, MongoDB with Mongoose. Prefer TypeScript and ES modules for a new server; preserve JavaScript/CommonJS in existing servers.
- Background work: Redis, BullMQ and ioredis when queued, delayed or retryable processing is required. Do not add infrastructure without a relevant feature.
- Uploads: save files under the server's persistent `uploads/`; store relative paths and resolve them through a configured media origin.
- Full mobile apps: Kotlin and Compose Multiplatform, Koin, Ktor, Coil and shared Material 3 theme/resources. Follow existing navigation and supporting libraries.
- Existing web app wrappers: load the approved URL using platform WebViews in Android and iOS.

Read actual installed versions and lockfiles. Do not replace libraries, convert the module system or migrate an existing app merely to align it with new-project defaults. Record an authorised exception in the brief without reopening the same approval.

## Implement within existing boundaries

Give a short approach before meaningful changes. Inspect neighbouring code and call sites. Reuse suitable components, services, DTOs, models and helpers. Keep changes focused, preserve unrelated user work and avoid speculative abstractions or unsolicited refactors.

Keep controllers thin and business logic in services. Validate body, query and path inputs; allowlist fields and enforce server-side authentication, role and record ownership. Preserve response contracts used by other clients. Check affected web, admin and mobile consumers when changing an API.

Use atomic updates or transactions where business consistency requires them. Prevent duplicate side effects in jobs, payments and callbacks. Treat queue enqueueing, provider acceptance and completed business work as separate states. Do not automatically retry unsafe mutations.

Use one API and media helper per client. Keep secrets out of client code, public environment variables, logs and documentation. Validate uploaded content, file limits and path containment. Public assets and private documents require different serving rules. Never hardcode credentials, client IDs, signing material or another project's endpoints.

In mobile code, keep Android and iOS SDK APIs in platform source sets. Use `expect`/`actual` at genuine platform boundaries and ordinary common code for shared behaviour. Use Koin constructor injection, lifecycle-owned state and reusable Ktor/Coil clients. Preserve coroutine cancellation. Do not send API credentials to arbitrary image hosts or treat ordinary preferences as a secure credential store.

Use shared typography, colours, dimensions and localized resources. Preserve accessibility and loading, empty and error states. For WebViews, handle approved external schemes, missing apps, payment return, back navigation and offline recovery explicitly. Do not bypass certificate errors.

## Verify according to the task

Read the brief's verification policy and the actual build scripts before running commands. If the owner requests manual testing only, do not run automated tests, introduce test automation or use aggregate tasks that include tests. Perform allowed build/compile checks and provide manual acceptance steps.

Otherwise run existing relevant checks proportionate to the change. Add meaningful regression coverage where needed for material behavioural risk; avoid tests that merely mirror implementation. Do not invent scripts or report placeholder test commands as passing tests. Do not run production seeds, live mutations or notification workers to verify a build.

Report exact module, command, result and limitations. Distinguish Android compile, iOS shared compile, Xcode host build and runtime testing. Never describe compilation as functional verification. If credentials, network access or a platform toolchain block a check, state that clearly and continue checks that remain valid.

## Finish and hand over

Review the final diff for scope, compatibility, secrets and unintended changes. Update configuration examples and documentation for meaningful contract or environment changes. Summarise the user-visible result, checks performed, manual verification steps and material remaining gaps.

Do not publish, deploy, perform destructive data changes, incur new paid commitments or send external messages without authorisation covering that action. Existing explicit authorisation is sufficient; do not ask again. For a decision still needed, prepare the concrete reviewable work first and ask only for the unresolved action.

Keep this entry point concise. Put project-specific details in `docs/project-brief.md`; put reusable engineering standards in `docs/developer-sop.md`.
