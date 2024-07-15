# Banking.Api

## Sobre

Versão teste para deploy em Kubernetes

## Deploy

1. Gerar a imagem docker
```bash
docker build -t banking-api:0.0.1 .
```

2. Subir a imagem para o repositório

```bash
docker tag banking-api:0.0.1 <seu-repositorio>/banking-api:0.0.1
docker push <seu-repositorio>/banking-api:0.0.1
```

3. Implantação no Kubernetes

```bash
kubectl apply -f k8s/banking.api.config.yaml
kubectl apply -f k8s/banking.api.deployment.yaml
```