@import hexlet.code.models.Page
@import io.javalin.http.Context

@param Page page
@param Context ctx

@template.layout.pageTmpl(content = @`
    <main class="flex-grow-1">

        <section>

            <div class="container-lg mt-5">
                <h1>Сайт: https://javalin.io</h1>

                <table class="table table-bordered table-hover mt-3">
                    <tbody>
                    <tr>
                        <td>ID</td>
                        <td>1</td>
                    </tr>
                    <tr>
                        <td>Имя</td>
                        <td>https://javalin.io</td>
                    </tr>
                    <tr>
                        <td>Дата создания</td>
                        <td>11/02/2025 19:43</td>
                    </tr>
                    </tbody>
                </table>

                <h2 class="mt-5">Проверки</h2>
                <form method="post" action="/urls/1/checks">
                    <button type="submit" class="btn btn-primary">Запустить
                        проверку
                    </button>
                </form>

                <table class="table table-bordered table-hover mt-3">
                    <thead>
                    <tr>
                        <th class="col-1">ID</th>
                        <th class="col-1">Код ответа</th>
                        <th>title</th>
                        <th>h1</th>
                        <th>description</th>
                        <th class="col-2">Дата проверки</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>

        </section>
    </main>`,
page = page,
ctx = ctx,
footer = @`
    <h1><a href="https://github.com/ZorgIT">GitHub</a></h1>
`)

