interface Consumivel {
    fun atualizarProgresso(valor: Int)
    fun tempoRestante(): String
}

abstract class Obra(
    val id: Int,
    val titulo: String,
    val autor: String
) {
    abstract fun exibirDetalhes()
}

class Livro(
    id: Int,
    titulo: String,
    autor: String,
    private val paginasTotal: Int
) : Obra(id, titulo, autor), Consumivel {

    private var paginaAtual = 0

    override fun atualizarProgresso(valor: Int) {
        if (valor > 0) {
            paginaAtual += valor

            if (paginaAtual > paginasTotal) {
                paginaAtual = paginasTotal
            }
        }
    }

    override fun tempoRestante(): String {
        return "${paginasTotal - paginaAtual} páginas restantes"
    }

    fun concluido(): Boolean {
        return paginaAtual >= paginasTotal
    }

    fun getPaginaAtual(): Int {
        return paginaAtual
    }

    fun getPaginasTotal(): Int {
        return paginasTotal
    }

    override fun exibirDetalhes() {
        println(
            """
            Livro
            ID: $id
            Título: $titulo
            Autor: $autor
            Progresso: $paginaAtual/$paginasTotal páginas
            """.trimIndent()
        )
    }
}

class AudioBook(
    id: Int,
    titulo: String,
    autor: String,
    private val duracaoTotal: Int
) : Obra(id, titulo, autor), Consumivel {

    private var duracaoAtual = 0

    override fun atualizarProgresso(valor: Int) {
        if (valor > 0) {
            duracaoAtual += valor

            if (duracaoAtual > duracaoTotal) {
                duracaoAtual = duracaoTotal
            }
        }
    }

    override fun tempoRestante(): String {
        return "${duracaoTotal - duracaoAtual} minutos restantes"
    }

    fun concluido(): Boolean {
        return duracaoAtual >= duracaoTotal
    }

    fun getDuracaoAtual(): Int {
        return duracaoAtual
    }

    fun getDuracaoTotal(): Int {
        return duracaoTotal
    }

    override fun exibirDetalhes() {
        println(
            """
            Audiobook
            ID: $id
            Título: $titulo
            Autor: $autor
            Progresso: $duracaoAtual/$duracaoTotal minutos
            """.trimIndent()
        )
    }
}

class RegistroLeitura(
    val obraId: Int,
    val comentario: String,
    val progressoRealizado: Int,
    val notaFinal: Int? = null
) {

    override fun toString(): String {
        return """
            Obra ID: $obraId
            Comentário: $comentario
            Progresso realizado: $progressoRealizado
            Nota final: ${notaFinal ?: "-"}
        """.trimIndent()
    }
}

class SistemaLeituras {

    private val obras = mutableListOf<Obra>()
    private val registros = mutableListOf<RegistroLeitura>()

    fun adicionarObra(obra: Obra) {
        obras.add(obra)
    }

    fun listarObras() {
        obras.forEach {
            println()
            it.exibirDetalhes()
        }
    }

    fun buscarObra(id: Int): Obra? {
        return obras.find { it.id == id }
    }

    fun registrarLeitura(
        idObra: Int,
        progresso: Int,
        comentario: String
    ) {

        val obra = buscarObra(idObra) ?: run {
            println("Obra não encontrada.")
            return
        }

        if (obra is Consumivel) {
            obra.atualizarProgresso(progresso)
        }

        registros.add(
            RegistroLeitura(
                obraId = idObra,
                comentario = comentario,
                progressoRealizado = progresso
            )
        )
    }

    fun listarRegistros() {
        registros.forEach {
            println()
            println(it)
        }
    }
}


fun main() {

    val sistema = SistemaLeituras()

    var opcao: Int

    do {

        println(
            """
            ===== MENU =====
            1 - Cadastrar Livro
            2 - Cadastrar Audiobook
            3 - Registrar Leitura
            4 - Listar Obras
            5 - Listar Registros
            0 - Sair
            """.trimIndent()
        )

        opcao = readln().toInt()

        when (opcao) {

            1 -> {
                print("ID: ")
                val id = readln().toInt()

                print("Título: ")
                val titulo = readln()

                print("Autor: ")
                val autor = readln()

                print("Total de páginas: ")
                val paginas = readln().toInt()

                sistema.adicionarObra(
                    Livro(id, titulo, autor, paginas)
                )
            }

            2 -> {
                print("ID: ")
                val id = readln().toInt()

                print("Título: ")
                val titulo = readln()

                print("Autor: ")
                val autor = readln()

                print("Duração total (minutos): ")
                val duracao = readln().toInt()

                sistema.adicionarObra(
                    AudioBook(id, titulo, autor, duracao)
                )
            }

            3 -> {
                print("ID da obra: ")
                val id = readln().toInt()

                print("Progresso realizado: ")
                val progresso = readln().toInt()

                print("Comentário: ")
                val comentario = readln()

                sistema.registrarLeitura(
                    id,
                    progresso,
                    comentario
                )
            }

            4 -> sistema.listarObras()

            5 -> sistema.listarRegistros()
        }

    } while (opcao != 0)
}