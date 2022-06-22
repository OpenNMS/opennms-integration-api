import router from '../router'
import { Router } from 'vue-router'

// for plugin routing, use this hook
const useRouter = (): Router => {
  return (window as any).VRouter || router
}

export default useRouter
