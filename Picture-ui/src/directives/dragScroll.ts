// dragScroll.ts
export default {
  mounted(el: HTMLElement) {
    let isDown = false
    let startX = 0
    let scrollLeft = 0

    el.style.cursor = 'grab'

    el.addEventListener('mousedown', (e) => {
      isDown = true
      startX = e.pageX - el.offsetLeft
      scrollLeft = el.scrollLeft
      el.style.cursor = 'grabbing'
      el.style.userSelect = 'none'
    })

    el.addEventListener('mouseleave', () => {
      isDown = false
      el.style.cursor = 'grab'
    })

    el.addEventListener('mouseup', () => {
      isDown = false
      el.style.cursor = 'grab'
    })

    el.addEventListener('mousemove', (e) => {
      if (!isDown) return
      e.preventDefault()
      const x = e.pageX - el.offsetLeft
      const walk = (x - startX) * 1.5 // 滚动速度
      el.scrollLeft = scrollLeft - walk
    })
  },
}
