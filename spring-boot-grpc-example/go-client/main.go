package main

import (
	"context"
	"fmt"
	"io"
	"log"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"google.golang.org/grpc/metadata"
	"google.golang.org/protobuf/types/known/emptypb"

	// You need to generate the Go protobuf code first.
	// Run: protoc --go_out=. --go_opt=paths=source_relative --go-grpc_out=. --go-grpc_opt=paths=source_relative *.proto
	"grpc-client/tdd_v1"
)

func main() {
	conn, err := grpc.Dial("localhost:9090", grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()

	client := tdd_v1.NewTdd_V1Client(conn)

	// Call Unary RPC
	callTLV1(client)

	// Call Unary RPC with Metadata
	callTLV1WithMetadata(client)

	// Call Server Streaming RPC
	callTLV2(client)
}

func callTLV1(client tdd_v1.Tdd_V1Client) {
	fmt.Println("\n--- Calling TLV1 (Unary) ---")
	resp, err := client.TLV1(context.Background(), &emptypb.Empty{})
	if err != nil {
		log.Fatalf("could not call TLV1: %v", err)
	}
	fmt.Printf("Response from TLV1: %s\n", resp.GetMessage())
}

func callTLV1WithMetadata(client tdd_v1.Tdd_V1Client) {
	fmt.Println("\n--- Calling TLV1 with Metadata ---")
	md := metadata.Pairs("authorization", "Bearer my-secret-token")
	ctx := metadata.NewOutgoingContext(context.Background(), md)

	resp, err := client.TLV1(ctx, &emptypb.Empty{})
	if err != nil {
		log.Fatalf("could not call TLV1 with metadata: %v", err)
	}
	fmt.Printf("Response from TLV1: %s\n", resp.GetMessage())
}

func callTLV2(client tdd_v1.Tdd_V1Client) {
	fmt.Println("\n--- Calling TLV2 (Server Streaming) ---")
	stream, err := client.TLV2(context.Background(), &tdd_v1.RequestForm{Req: "Stream Request"})
	if err != nil {
		log.Fatalf("could not call TLV2: %v", err)
	}

	for {
		resp, err := stream.Recv()
		if err == io.EOF {
			fmt.Println("Stream finished.")
			break
		}
		if err != nil {
			log.Printf("Error while reading stream: %v", err)
			break
		}
		fmt.Printf("Received stream message: %s\n", resp.GetMessage())
	}
}
