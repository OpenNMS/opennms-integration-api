const addStylesheet = (url: string) => {
  const head = document.head
  const link = document.createElement("link")

  link.type = "text/css"
  link.rel = "stylesheet"
  link.href = url

  head.appendChild(link)
}

export default addStylesheet
