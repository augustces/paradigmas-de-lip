interface Detalhes {
    fun exibirDetalhes(): String
}

abstract class Obra(
    var id: Int,
    val tipo: String,
    val titulo: String,
    val autor: String
): Detalhes {
    abstract fun atualizarProgresso(valor: Int): String
    abstract fun tempoRestante(): String
    abstract fun setId(newId:Int)
}

class Livro(
    titulo: String,
    autor: String,
    private val paginasTotal: Int
) : Obra(-1,"livro", titulo, autor), Detalhes {

    private var paginaAtual = 0

    override fun atualizarProgresso(valor: Int): String {
        if (valor > 0) {
            if (paginasTotal > (paginaAtual + valor)){
                return "Quantidade de páginas lidas ultrapassa páginas totais do livro"
            }
            paginaAtual += valor

            return "Sucesso"
        }
        return "Valor inválido de páginas lidas"
    }

    override fun tempoRestante(): String {
        return "${paginasTotal - paginaAtual} páginas restantes"
    }

    fun concluido(): Boolean {
        return paginaAtual == paginasTotal
    }

    fun getPaginaAtual(): Int {
        return paginaAtual
    }

    fun getPaginasTotal(): Int {
        return paginasTotal
    }

    override fun setId(newId: Int){
        this.id = newId
    }

    override fun exibirDetalhes(): String {
        return """
            Livro
            ID: $id
            Título: $titulo
            Autor: $autor
            Progresso: $paginaAtual/$paginasTotal páginas
            """.trimIndent()

    }
}

class AudioBook(
    titulo: String,
    autor: String,
    private val duracaoTotal: Int
) : Obra(-1, "audiobook", titulo, autor), Detalhes {

    private var duracaoAtual = 0

    override fun atualizarProgresso(valor: Int): String {
        if (valor > 0) {
            if (duracaoTotal > (duracaoAtual + valor)){
                return "Quantidade de minutos escutados ultrapassa páginas totais do audiobook"
            }
            duracaoAtual += valor

            return "Sucesso"
        }
        return "Valor inválido de páginas lidas"
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

    override fun setId(newId: Int){
        this.id = newId
    }

    override fun exibirDetalhes(): String{
        return """
            Audiobook
            ID: $id
            Título: $titulo
            Autor: $autor
            Progresso: $duracaoAtual/$duracaoTotal minutos
            """.trimIndent()

    }
}

class RegistroLeitura(
    val obraId: Int,
    val progressoRealizado: Int,
    val notaFinal: Int? = null
) {

    override fun toString(): String {
        return """
            Obra ID: $obraId
            Progresso realizado: $progressoRealizado
            Nota final: ${notaFinal ?: "-"}
        """.trimIndent()
    }
}

class SistemaLeituras {

    private val obras = mutableListOf<Obra>()
    private val registros = mutableListOf<RegistroLeitura>()
    private var indexLivro:Int = 0
    private var indexAudioBook:Int = 0

    fun adicionarObra(obra: Obra, op: Int) {
        if (op == 1){
            obra.setId(indexLivro)
            indexLivro += 1
        }
        if (op==2){
            obra.setId(indexAudioBook)
            indexAudioBook +=1
        }
        obras.add(obra)
    }

    fun listarObras() {
        obras.forEach {
            println()
            it.exibirDetalhes()
        }
    }

    fun buscarObra(id: Int, tipo: String): Obra? {
        val encontrados = obras.filter { it.tipo == tipo }
        return encontrados.find { it.id == id }
    }

    fun registrarLeitura(
        idObra: Int,
        progresso: Int,
        tipo: String
    ): String {

        val obra = buscarObra(idObra, tipo)
        if (obra == null) {
            return ("Obra não encontrada.")

        }

        val result = obra.atualizarProgresso(progresso)
        if (result == "Sucesso")
            registros.add(
                RegistroLeitura(
                    obraId = idObra,
                    progressoRealizado = progresso
                )
            )



        return result
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

                print("Título: ")
                val titulo = readln()

                print("Autor: ")
                val autor = readln()

                print("Total de páginas: ")
                val paginas = readln().toInt()

                sistema.adicionarObra(
                    Livro(titulo, autor, paginas), 1
                )
            }

            2 -> {

                print("Título: ")
                val titulo = readln()

                print("Autor: ")
                val autor = readln()

                print("Duração total (minutos): ")
                val duracao = readln().toInt()

                sistema.adicionarObra(
                    AudioBook(titulo, autor, duracao), 2
                )
            }

            3 -> {
                println(
                    """
            Tipo da obra:
            1. Livro
            2. AudioBook
            """.trimIndent()
                )

                val tipoInt = readln().toInt()
                var tipo = ""
                if (tipoInt == 1)
                    tipo = "livro"
                if (tipoInt == 2)
                    tipo = "audiobook"
                else
                    println("Tipo inválido")
                if (tipoInt == 1 || tipoInt == 2){
                    print("ID da obra: ")
                    val id = readln().toInt()

                    print("Progresso realizado: ")
                    val progresso = readln().toInt()

                    sistema.registrarLeitura(
                        id,
                        progresso,
                        tipo
                    )
                }


            }

            4 -> sistema.listarObras()

            5 -> sistema.listarRegistros()
        }

    } while (opcao != 0)
}