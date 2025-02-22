@import hexlet.code.models.Page
@import io.javalin.http.Context

@param Page page
@param Context ctx

@template.layout.pageTmpl(
content = @`
    <section class="dark-section">
        <div class="container-fluid bg-dark p-3">
            <div class="row g-0"> <!-- Добавлен g-0 для убирания gutter -->
                <div class="col-md-10 col-lg-8 mx-auto text-white">
                    <h1 class="display-3 mt-0 mb-2">Анализатор страниц</h1>
                    <p class="lead mb-4">Бесплатно проверяйте сайты на SEO пригодность</p>
                    <form action="/urls" method="post" class="rss-form text-body">
                        <div class="row g-2">
                            <div class="col">
                                <div class="form-floating">
                                    <input id="url-input"
                                           autofocus
                                           type="text"
                                           required
                                           name="url"
                                           aria-label="url"
                                           class="form-control"
                                           placeholder="ссылка"
                                           autocomplete="off">
                                    <label for="url-input">Ссылка</label>
                                </div>
                            </div>
                            <div class="col-auto">
                                <button type="submit" class="btn btn-primary btn-lg h-100 w-100">
                                    Проверить
                                </button>
                            </div>
                        </div>
                    </form>
                    <p class="mt-2 mb-0 text-muted">Пример: https://www.example.com</p>
                </div>
            </div>
        </div>
    </section>
`,
page = page,
footer = null,
ctx = ctx
)