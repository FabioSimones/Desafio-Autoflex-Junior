import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' }
})

api.interceptors.response.use(
  (resp) => resp,
  (error) => {
    if (error.response?.data) return Promise.reject(error.response.data)
    if (error.request) return Promise.reject({ mensagem: 'Servidor indisponível' })
    return Promise.reject({ mensagem: 'Erro inesperado' })
  }
)

export default api
