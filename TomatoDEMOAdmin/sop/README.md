# Codemind Studio SOP pack

This pack gives the team one engineering baseline and keeps each client's requirements separate. The Word document is for sharing and onboarding; the Markdown SOP contains the same policy for repository use.

## Files

- `Codemind-Studio-Developer-SOP.docx`: team-readable SOP.
- `docs/developer-sop.md`: full SOP for the repository.
- `AGENTS.md`: concise agent entry point.
- `docs/project-brief.template.md`: copy and complete separately for each project.
- `docs/project-review.md`: evidence and differences from the reviewed local projects.

## Install into a project

1. Copy `docs/developer-sop.md` into the target repository.
2. Copy `docs/project-brief.template.md` as `docs/project-brief.md` and fill in the project facts, module paths and verification policy.
3. Add the supplied `AGENTS.md` at the repository root. If one already exists, merge the relevant instructions and preserve project-specific rules; do not overwrite it blindly.
4. Check nested instructions and any `AGENTS.override.md` for conflicts. Start a fresh agent session and ask it to name the active instructions and summarise the brief before implementation.
5. Share the Word SOP with the developer, assign a small supervised task, and review the implementation and evidence.

For separate API, web and mobile repositories, place an entry point in each and record companion repository paths in the brief. Keep the SOP version aligned across copies. Changes to this pack do not automatically update existing projects.

Official documentation describes Codex instruction discovery and directory overrides: [Custom instructions with AGENTS.md](https://learn.chatgpt.com/docs/agent-configuration/agents-md). Other coding agents should use their supported instruction-file mechanism with an explicit pointer to the SOP and brief.

## Starter prompt

```text
Read AGENTS.md, docs/developer-sop.md and docs/project-brief.md.
Inspect the relevant existing code and installed dependencies.
Summarise the applicable stack and any approved exceptions.
Ask only unresolved questions that materially affect the requested work.
Then implement the task within the agreed scope and verification policy.
Task: [describe the actual feature or fix]
```

## Working rule

New admin panels default to Next.js. Existing Vite panels retain their stack unless migration is authorised. Record client-specific exceptions in the project brief; the rest of the SOP still applies. A build result and a manual functional result must always be reported separately.

Version 1.0 • 8 October 2026
