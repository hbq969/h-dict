/// <reference types="vite/client" />

interface ImportMetaEnv {
  // 请求base路径
  VITE_API_URL: string
  // 是否debug模式
  VITE_DEBUG_MODE: boolean
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
