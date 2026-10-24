# 项目开发指南

## 分支同步（强制）

- 本项目**开发分支（主分支）**为 `SpringBoot3.x`；**每次提交后必须立即 pick 到目标分支，不必等用户提醒**：`SpringBoot3.x` → `master`（`git cherry-pick <sha>`）。
- 注意：本仓库开发分支名为 `SpringBoot3.x`（不是 SpringBoot3.5.4）。
- pick 后必须核对：目标分支 `git log --oneline -1` 与开发分支提交信息一致、无冲突标记、`git status` 干净。
- 若 pick 冲突：先解决冲突并在提交说明里写明冲突文件，**不得直接丢弃目标分支的改动**。
- 推送以用户明确授权为准；用户授权推送时，**开发分支与目标分支一起推**（AI 不擅自 push）。

