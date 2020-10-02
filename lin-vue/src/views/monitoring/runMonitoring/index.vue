<template>
    <iframe style="width: 100%;min-height: 1000px" :src="itemUrl"></iframe>
</template>

<script>
    import {baseURL} from '@/settings'
    export default {
        name: "index",
        data(){
            return{
                itemUrl : baseURL + "/druid/index.html"
            }
        },
        computed:{
        }
    }
</script>

<style scoped>

</style>
