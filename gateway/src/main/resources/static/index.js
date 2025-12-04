async function loadI18n() {
    const lang = navigator.language
    try {
        const dict = await fetch(`/i18n/${lang}.json`).then(r => r.json());
        document.querySelectorAll('[data-i18n]').forEach(el => {
            const key = el.getAttribute('data-i18n');
            if (dict[key]) el.innerText = dict[key];
        });
    } catch (e) {
        console.warn('i18n file not found for lang:', lang);
    }
}

async function loadServices() {
    try {
        const services = await fetch('/docs/services').then(r => r.json());
        const list = document.getElementById('services-list');

        for (const svc of services) {
            const instances = await fetch(`/docs/instances/${svc}`).then(r => r.json());
            instances.forEach(inst => {
                const url = `http://${inst.host}:${inst.port}/swagger-ui/index.html`;
                const li = document.createElement('li');

                li.innerHTML = `
                    <details>
                        <summary>${svc}</summary>
                        <div class="collapsible-content" id="routes-${svc}-${inst.port}">
                            <p>Loading...</p>
                        </div>
                    </details>
                `;

                list.appendChild(li);

                fetch(`/docs/openapi/${svc}`)
                    .then(r => r.json())
                    .then(sw => {
                        const routesDiv =
                            document.getElementById(`routes-${svc}-${inst.port}`) ||
                            document.getElementById(`routes-${svc}`);

                        routesDiv.innerHTML = "";
                        if (!sw.paths) {
                            routesDiv.innerHTML = "<p>No routes found</p>";
                            return;
                        }
                        Object.entries(sw.paths).forEach(([path, methods]) => {
                            Object.entries(methods).forEach(([method, data]) => {
                                const routeEl = document.createElement("div");
                                routeEl.style.margin = "6px 0";
                                routeEl.innerHTML = `<strong>${method.toUpperCase()}</strong> — ${path}`;
                                routesDiv.appendChild(routeEl);
                            });
                        });
                    })
                    .catch((e) => {
                        console.log(e)
                        const routesDiv = document.getElementById(`routes-${svc}`);
                        routesDiv.innerHTML = "<p>Failed to load routes</p>";
                    });
            });
        }
    } catch (error) {
        console.error('Failed to load services:', error);
    }
}

loadI18n().then(_r => loadServices());
