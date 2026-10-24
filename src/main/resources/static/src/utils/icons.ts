import {defineComponent, h} from 'vue'
import {Icon} from '@iconify/vue'

// 把 Iconify（lucide）图标包成组件，供 el-button :icon / el-icon 使用；
// 图标尺寸由父级字号决定（1em），跟随 Element Plus 三档字号。
const make = (name: string) => defineComponent({
  name: 'Ic' + name.replace(/(^|-)(\w)/g, (_, __, c) => c.toUpperCase()),
  setup: () => () => h(Icon, {icon: `lucide:${name}`, width: '1em', height: '1em'})
})

export const IcEdit = make('pencil')
export const IcPlus = make('plus')
export const IcDelete = make('trash')
export const IcSearch = make('search')
export const IcRefresh = make('refresh-cw')
export const IcClose = make('x')
export const IcCheck = make('check')
export const IcView = make('eye')
export const IcDownload = make('download')
export const IcUpload = make('upload')
export const IcSetting = make('settings')
export const IcArrowLeft = make('arrow-left')
export const IcMore = make('ellipsis')
export const IcCopy = make('copy')
export const IcWarning = make('triangle-alert')
export const IcInfo = make('info')

export const IcFilter = make('filter')
export const IcMaximize = make('maximize')
export const IcMinimize = make('minimize')
export const IcReset = make('rotate-ccw')
