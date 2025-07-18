import { ref } from 'vue'

export function useDebounce<T extends (...args: any[]) => Promise<any>>(fn: T, delay = 300) {
  const timer = ref<number | null>(null)

  return (...args: Parameters<T>): Promise<ReturnType<T>> => {
    return new Promise((resolve) => {
      if (timer.value) {
        clearTimeout(timer.value)
      }
      timer.value = window.setTimeout(async () => {
        const res = await fn(...args)
        resolve(res)
      }, delay)
    })
  }
}
