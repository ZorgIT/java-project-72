@import hexlet.code.models.Page
@import io.javalin.http.Context

@param Page page
@param Context ctx

@template.layout.pageTmpl(content = @`
    <main class="flex-grow-1">
        <section >
            <div class="container-lg mt-5" style="min-height: 400px;">
                <h1>Сайты</h1>

                <table class="table table-bordered table-hover mt-3">
                    <thead>
                    <tr>
                        <th class="col-1">ID</th>
                        <th>Имя</th>
                        <th class="col-2">Последняя проверка</th>
                        <th class="col-1">Код ответа</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>

        </section>
    </main>`,
page = page,
footer = null,
ctx = ctx
)

