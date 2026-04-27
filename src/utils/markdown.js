import { marked } from 'marked'

marked.setOptions({
  gfm: true,
  breaks: true,
})

export function renderMarkdown(content = '') {
  if (!content) return ''

  try {
    return marked.parse(content)
  } catch {
    return content.replace(/\n/g, '<br />')
  }
}

export function toPlainExcerpt(content = '', maxLength = 120) {
  if (!content) return ''

  const plainText = content
    .replace(/```[\s\S]*?```/g, ' ')
    .replace(/`([^`]+)`/g, '$1')
    .replace(/!\[.*?\]\(.*?\)/g, ' ')
    .replace(/\[(.*?)\]\(.*?\)/g, '$1')
    .replace(/[#>*_\-\n\r]/g, ' ')
    .replace(/\s+/g, ' ')
    .trim()

  if (plainText.length <= maxLength) {
    return plainText
  }

  return `${plainText.slice(0, maxLength).trim()}...`
}
