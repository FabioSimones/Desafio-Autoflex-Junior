import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../pages/HomePage.vue'
import ProdutosPage from '../pages/ProdutosPage.vue'
import MateriasPrimasPage from '../pages/MateriasPrimasPage.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: HomePage },
    { path: '/produtos', component: ProdutosPage },
    { path: '/materias-primas', component: MateriasPrimasPage }
  ]
})

export default router
