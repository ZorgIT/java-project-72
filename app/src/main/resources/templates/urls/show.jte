@import hexlet.code.dto.urls.UrlPage
@import io.javalin.http.Context

@param UrlPage page
@param Context ctx


@template.layout.pageTmpl(content = @`
    <main class="flex-grow-1">
        <section>
            <div class="container-lg mt-5">
                <h1>Сайт: ${page.getUrl().getName()}</h1>

                <table class="table table-bordered table-hover mt-3">
                    <tbody>
                    <tr>
                        <td>ID</td>
                        <td>${page.getUrl().getId()}</td>
                    </tr>
                    <tr>
                        <td>Имя</td>
                        <td>${page.getUrl().getName()}</td>
                    </tr>
                    <tr>
                        <td>Дата создания</td>
                        <td>${page.getUrl().getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}</td>
                    </tr>
                    </tbody>
                </table>
                <h2 class="mt-5">Проверки</h2>
                <form method="post"
                      action="/urls/${page.getUrl().getId()}/checks">
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
                    @if (page.getUrlChecks() != null && !page.getUrlChecks().isEmpty())
                        <tbody>
                        @for(var urlCheck: page.getUrlChecks())
                            <tr>
                            <td>${urlCheck.getId()}</td>
                            <td>${urlCheck.getStatusCode()}</td>
                            <td>
                                @if(urlCheck.getTitle() != null)
                                    ${urlCheck.getTitle()}
                                @endif
                            </td>
                            <td>
                                @if(urlCheck.getH1() != null)
                                    ${urlCheck.getH1()}
                                @endif
                            </td>
                            <td>
                                @if(urlCheck.getDescription() != null)
                                    ${urlCheck.getDescription()}
                                @endif
                            </td>
                            <td>${urlCheck.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}</td>
                            </tr>
                        @endfor
                        </tbody>
                    @endif
                </table>
            </div>
        </section>
    </main>`,
page = page,
ctx = ctx,
footer = null)

