import {defineStore} from 'pinia'

export const useAppStore = defineStore('app', {
    state: () => ({
        msg: 'hello pinia',
        authentication: '',
    }),
    getters: {
        getAuthentication: (state) => state.authentication,
    },
    actions: {
        setAuthentication(authentication: string) {
            this.authentication = authentication
        },
    }
})
