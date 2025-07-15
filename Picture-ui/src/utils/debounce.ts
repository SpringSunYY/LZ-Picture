export function useDebounce(fn: Function, delay: number) {
  let timer: ReturnType<typeof setTimeout> | null = null
  return (...args: any[]) => {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => {
      fn.apply(this, args)
    }, delay)
  }
}
