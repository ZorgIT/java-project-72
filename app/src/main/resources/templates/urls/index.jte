@import hexlet.code.dto.urls.UrlsPage
@import io.javalin.http.Context
@import gg.jte.Content

@param UrlsPage page
@param Context ctx
@param Content footer

@template.layout.pageTmpl(
content = @`
    <main class="flex-grow-1">
        <section>
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
                    @for(var url: page.getUrls())
                        <tr>
                            <td>${url.getId()}</td>
                            <td>
                                <a href="/urls/${url.getId()}">${url.getName()}</a>
                            </td>
                            <td>
                                @if(url.getCheckedAt() != null)
                                    url.getCheckedAt().toString()
                                @endif
                            </td>
                            <td>
                                @if(url.getResponseCode() != null)
                                    url.getResponseCode()
                                @endif
                            </td>
                        </tr>
                    @endfor
                    </tbody>
                </table>
            </div>
        </section>
    </main>`,
page = page,
footer = footer,
ctx = ctx
)
