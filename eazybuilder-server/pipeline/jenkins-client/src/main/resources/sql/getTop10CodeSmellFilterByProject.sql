SELECT 
    sum(val) as total,p.name as project
FROM
    ci_metric m,
    (SELECT 
        MAX(a.id) AS mid, a.type,c.name
    FROM
        ci_metric a, ci_pipeline_history b, ci_project c
    WHERE
        a.pipeline_id = b.id
            AND b.project_id = c.id
            AND a.type in ('code_smell_blocker','code_smell_critical')
            AND c.id in (%s)
    GROUP BY c.name,a.type) p
WHERE
    m.id = p.mid
group by p.name
order by total desc 
limit 10