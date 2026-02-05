<template>
  <div class="min-h-screen bg-slate-50">
    <nav class="border-b border-slate-200 bg-white">
      <div class="mx-auto flex max-w-6xl items-center justify-between px-4 py-3">
        <div class="font-semibold text-slate-900">Autoflex</div>
        <div class="flex gap-3 text-sm">
          <router-link to="/produtos" class="hover:text-blue-600">Produtos</router-link>
          <router-link to="/materias-primas" class="hover:text-blue-600">Matérias primas</router-link>
          <router-link to="/sugestao-producao" class="hover:text-blue-600">Sugestão de produção</router-link>
        </div>
      </div>
    </nav>

    <main>
      <router-view />
    </main>
  </div>
</template>
