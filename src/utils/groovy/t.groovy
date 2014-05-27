String.metaInfo.scan = { regex ->
    (delegate =~ regex).collect { it }[1..-1]
}

def (a, b, c) = "abcdefghijklmnop".scap

println asMap('''\
    a
      x
      [b]
        c
        [d]
          e''')

// @Test
def testConditional() {
    def original = asTree '''\
    a/
      x
      [b]/
        c
        [d]/
          e
    '''

    def expected = asTree '''\
    a/
      x
      c
      e
    '''

}

Map asMap(String text) {
    asMap('', [:], text.stripMargin().split(/\n/).toList().reverse())
}

Map asMap(String parentIndent, Map parent, List lines) {
    def prevIndent = null
    while(lines) {
        def line = lines.pop()
        def (indent, content) = splitLine(line)
        if(indent <= parentIndent) {
            return parent
        } else if(!prevIndent || indent == prevIndent) {
            parent[content] = ''
        } else {
            lines.push(line)
            parent[content] = asMap(prevIndent, [:], lines)
        }
        prevIndent = indent
        prevContent = content
    }
    return parent
}

def splitLine(line) {
    def split = []
    (line =~ /(\s*)(\S.*)/).each { _, indent, content ->
        split = [indent, content]
    }
    println "split: $split"
    split
}

