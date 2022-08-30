{application, 'cowlib', [
	{description, "Support library for manipulating Web protocols."},
	{vsn, "2.7.0"},
	{id, "v3.7.16"},
	{modules, ['cow_base64url','cow_cookie','cow_date','cow_hpack','cow_http','cow_http2','cow_http2_machine','cow_http_hd','cow_http_te','cow_iolists','cow_mimetypes','cow_multipart','cow_qs','cow_spdy','cow_sse','cow_uri','cow_ws']},
	{registered, []},
	{applications, [kernel,stdlib,crypto]},
	{env, []}
]}.